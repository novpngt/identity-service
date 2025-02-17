package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.requests.UserUpdateRequest;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private UserService userService;

    @PostMapping()
    ApiResponse<User, Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<User, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse getAllUsers() {
        ApiResponse<List<User>, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse getUserById(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    ApiResponse updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUserById(@PathVariable("userId") String userId) {
        ApiResponse<String, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.deleteUser(userId));
        return apiResponse;
    }
}
