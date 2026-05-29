package com.ticketing.service.impl;

import com.ticketing.dto.request.CreateCategoryRequest;
import com.ticketing.dto.response.CategoryResponse;
import com.ticketing.dto.response.UserSummaryResponse;
import com.ticketing.entity.Category;
import com.ticketing.entity.User;
import com.ticketing.entity.enums.RoleName;
import com.ticketing.exception.ResourceAlreadyExistsException;
import com.ticketing.exception.UserNotFoundException;
import com.ticketing.repository.CategoryRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getAllAgents() {
        return userRepository.findByRoles_Name(RoleName.ROLE_SUPPORT_AGENT).stream()
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        user.setActive(!user.isActive());
        userRepository.save(user);
        
        return "User status toggled. Active: " + user.isActive();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists: " + request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();

        categoryRepository.save(category);

        return mapToCategoryResponse(category);
    }

    private UserSummaryResponse mapToUserSummary(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.isActive())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
