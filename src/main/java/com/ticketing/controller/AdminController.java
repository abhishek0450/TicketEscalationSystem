package com.ticketing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: implemented in Phase X
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/settings")
    public ResponseEntity<String> getSystemSettings() {
        return ResponseEntity.ok("Settings Skeleton");
    }
}
