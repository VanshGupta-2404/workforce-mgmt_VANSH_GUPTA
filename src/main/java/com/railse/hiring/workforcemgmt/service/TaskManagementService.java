package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface TaskManagementService {
    String addComment(TaskCommentRequest request);
    List<TaskCommentDto> getActivityHistory(Long taskId);

    List<TaskManagementDto> createTasks(TaskCreateRequest request);

    List<TaskManagementDto> getTasksByPriority(Priority priority); // Only once

    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);

    String assignByReference(AssignByReferenceRequest request);

    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);

    TaskManagementDto findTaskById(Long id);

    TaskManagementDto updateTaskPriority(TaskPriorityUpdateRequest request);

    List<TaskManagementDto> fetchSmartTasksByDate(TaskFetchByDateRequest request);
}
