package com.railse.hiring.workforcemgmt.controller;

import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.service.TaskCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/task-mgmt/tasks")
public class TaskCommentController {

    private final TaskCommentService commentService;

    public TaskCommentController(TaskCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<TaskComment> addComment(
            @PathVariable Long taskId,
            @RequestBody TaskComment comment) {
        TaskComment saved = commentService.addComment(taskId, comment);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<TaskComment>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }
}
