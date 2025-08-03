package com.railse.hiring.workforcemgmt.controller;

import com.railse.hiring.workforcemgmt.model.ActivityHistory;
import com.railse.hiring.workforcemgmt.repository.ActivityHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/activity")
public class ActivityHistoryController {

    private final ActivityHistoryRepository activityRepository;

    public ActivityHistoryController(ActivityHistoryRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @GetMapping
    public ResponseEntity<List<ActivityHistory>> getHistory(@PathVariable Long taskId) {
        return ResponseEntity.ok(activityRepository.findByTaskIdOrderByTimestampDesc(taskId));
    }
}
