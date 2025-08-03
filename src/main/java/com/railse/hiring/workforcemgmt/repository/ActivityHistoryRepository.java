package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
    List<ActivityHistory> findByTaskIdOrderByTimestampDesc(Long taskId);
}
