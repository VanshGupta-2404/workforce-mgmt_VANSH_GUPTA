package com.railse.hiring.workforcemgmt.dto;

import java.time.Instant;

// Response DTO (optional)
public class TaskCommentDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String comment;
    private Instant timestamp;


    /**
     * get field
     *
     * @return id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * set field
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

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

    /**
     * get field
     *
     * @return timestamp
     */
    public Instant getTimestamp() {
        return this.timestamp;
    }

    /**
     * set field
     *
     * @param timestamp
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
