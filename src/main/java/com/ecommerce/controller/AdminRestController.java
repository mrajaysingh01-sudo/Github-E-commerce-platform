package com.ecommerce.controller;

import com.ecommerce.model.ActivityLog;
import com.ecommerce.repository.ActivityLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdminRestController {

    private final ActivityLogRepository activityLogRepository;

    public AdminRestController(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    // Used by JavaScript fetch polling to auto-refresh system logs
    @GetMapping("/activity")
    public List<ActivityLog> getRecentActivity() {
        return activityLogRepository.findTop50ByOrderByTimestampDesc();
    }
}