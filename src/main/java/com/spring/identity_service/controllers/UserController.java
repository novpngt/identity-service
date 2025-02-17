package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.requests.UserUpdateRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private UserService userService;

    @PostMapping()
    ApiResponse<UserResponse, Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<UserResponse, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse getAllUsers() {
        ApiResponse<List<UserResponse>, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse, Void> getUserById(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse, Void>builder().data(userService.updateUser(userId, request)).build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUserById(@PathVariable("userId") String userId) {
        ApiResponse<String, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.deleteUser(userId));
        return apiResponse;
    }
}
