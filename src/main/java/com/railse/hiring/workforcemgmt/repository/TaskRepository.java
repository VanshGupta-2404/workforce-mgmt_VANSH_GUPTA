package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    TaskManagement save(TaskManagement task);

    Optional<TaskManagement> findById(Long id);

    List<TaskManagement> findAll();

    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);

    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
}
