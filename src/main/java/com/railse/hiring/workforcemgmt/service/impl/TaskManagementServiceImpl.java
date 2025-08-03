package com.railse.hiring.workforcemgmt.service.impl;

import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.repository.TaskCommentRepository;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {
    private final List<TaskManagement> taskStore = new ArrayList<>();
    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;
    private final TaskCommentRepository commentRepository;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper, TaskCommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public String addComment(TaskCommentRequest request) {
        TaskComment comment = new TaskComment();
        comment.setTaskId(request.getTaskId());
        comment.setUserId(request.getUserId());
        comment.setComment(request.getComment());
        comment.setTimestamp(Instant.now());
        commentRepository.save(comment);
        return "Comment added successfully";
    }

    @Override
    public List<TaskCommentDto> getActivityHistory(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream().map(c -> {
            TaskCommentDto dto = new TaskCommentDto();
            dto.setId(c.getId());
            dto.setTaskId(c.getTaskId());
            dto.setUserId(c.getUserId());
            dto.setComment(c.getComment());
            dto.setTimestamp(c.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            createdTasks.add(taskRepository.save(newTask));
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            if (!tasksOfType.isEmpty()) {
                for (TaskManagement taskToUpdate : tasksOfType) {
                    taskToUpdate.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToUpdate);
                }
            }

            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            taskRepository.save(newTask);
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task ->
                        task.getStatus() != TaskStatus.CANCELLED &&
                                task.getTaskDeadlineTime() >= request.getStartDate() &&
                                task.getTaskDeadlineTime() <= request.getEndDate()
                )
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public List<TaskManagementDto> fetchSmartTasksByDate(TaskFetchByDateRequest request) {
        List<Long> assigneeIds = request.getAssigneeIds();

        if (assigneeIds == null || assigneeIds.isEmpty()) {
            throw new IllegalArgumentException("assigneeIds cannot be null or empty.");
        }

        long start = request.getStartDate();
        long end = request.getEndDate();

        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(assigneeIds);

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> {
                    boolean isActive = task.getStatus() != TaskStatus.CANCELLED && task.getStatus() != TaskStatus.COMPLETED;
                    boolean inRange = task.getTaskDeadlineTime() >= start && task.getTaskDeadlineTime() <= end;
                    boolean overdueButStillOpen = task.getTaskDeadlineTime() < start && isActive;
                    return isActive && (inRange || overdueButStillOpen);
                })
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findAll();

        List<TaskManagement> filtered = tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filtered);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();

        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }

            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }

            if (item.getPriority() != null) {
                task.setPriority(item.getPriority());
            }

            updatedTasks.add(taskRepository.save(task));
        }

        return taskMapper.modelListToDtoList(updatedTasks);
    }

    @Override
    public TaskManagementDto updateTaskPriority(TaskPriorityUpdateRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + request.getTaskId()));

        task.setPriority(request.getNewPriority());

        // Optional: Append to activity history if supported
        if (task.getActivityHistory() != null) {
            task.getActivityHistory().add("Priority changed to " + request.getNewPriority());
        }

        TaskManagement updated = taskRepository.save(task);
        return taskMapper.modelToDto(updated);
    }


}
