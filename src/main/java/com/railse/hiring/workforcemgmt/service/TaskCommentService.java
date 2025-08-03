package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.repository.TaskCommentRepository;
import com.railse.hiring.workforcemgmt.model.ActivityHistory;
import com.railse.hiring.workforcemgmt.repository.ActivityHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskCommentService {

    private final TaskCommentRepository commentRepository;
    private final ActivityHistoryRepository historyRepository;

    public TaskCommentService(TaskCommentRepository commentRepository,
                              ActivityHistoryRepository historyRepository) {
        this.commentRepository = commentRepository;
        this.historyRepository = historyRepository;
    }

    public TaskComment addComment(Long taskId, TaskComment comment) {
        comment.setTaskId(taskId);
        comment.setTimestamp(Instant.now());  // ✅ Correct
        TaskComment saved = commentRepository.save(comment);

        // Log activity
        ActivityHistory activity = new ActivityHistory();
        activity.setTaskId(taskId);
        activity.setAction("Comment added: " + comment.getCommentText());
        activity.setTimestamp(LocalDateTime.now());
        historyRepository.save(activity);

        return saved;
    }

    public List<TaskComment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
