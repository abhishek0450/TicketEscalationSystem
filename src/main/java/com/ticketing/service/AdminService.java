package com.ticketing.service;

import com.ticketing.dto.request.CreateCategoryRequest;
import com.ticketing.dto.response.CategoryResponse;
import com.ticketing.dto.response.UserSummaryResponse;
import java.util.List;

public interface AdminService {
    List<UserSummaryResponse> getAllUsers();
    List<UserSummaryResponse> getAllAgents();
    String toggleUserStatus(Long userId);
    List<CategoryResponse> getAllCategories();
    CategoryResponse createCategory(CreateCategoryRequest request);
}
