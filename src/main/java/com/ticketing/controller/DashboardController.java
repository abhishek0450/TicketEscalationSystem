package com.ticketing.controller;

import com.ticketing.dto.response.DashboardStatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: implemented in Phase X
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok().build();
    }
}
