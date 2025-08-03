package com.railse.hiring.workforcemgmt.dto;

import java.time.Instant;

// Request to add comment
public class TaskCommentRequest {
    private Long taskId;
    private Long userId;
    private String comment;


    /**
     * get field
     *
     * @return taskId
     */
    public Long getTaskId() {
        return this.taskId;
    }

    /**
     * set field
     *
     * @param taskId
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * get field
     *
     * @return userId
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * set field
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * get field
     *
     * @return comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * set field
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}

