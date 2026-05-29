package com.ticketing.controller;

import com.ticketing.dto.request.CreateCategoryRequest;
import com.ticketing.dto.response.CategoryResponse;
import com.ticketing.dto.response.UserSummaryResponse;
import com.ticketing.service.AdminService;
import com.ticketing.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    // List all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> getAllUsers() {
        log.debug("Admin requesting list of all registered users");
        List<UserSummaryResponse> response = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", response));
    }

    // List all agents only
    @GetMapping("/users/agents")
    public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> getAllAgents() {
        log.debug("Admin requesting list of all registered support agents");
        List<UserSummaryResponse> response = adminService.getAllAgents();
        return ResponseEntity.ok(ApiResponse.success("All support agents retrieved successfully", response));
    }

    // Toggle user active/inactive
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<String>> toggleUserStatus(@PathVariable Long id) {
        log.debug("Admin toggling status of user ID: {}", id);
        String response = adminService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("User status toggled successfully", response));
    }

    // List all categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        log.debug("Admin requesting list of all ticket categories");
        List<CategoryResponse> response = adminService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("All categories retrieved successfully", response));
    }

    // Create category
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        log.debug("Admin creating a new category: {}", request.getName());
        CategoryResponse response = adminService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", response));
    }
}
