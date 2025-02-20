package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.requests.UserUpdateRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>, Void> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ApiResponse.<List<UserResponse>, Void>builder()
                .data(users)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse, ?> getUserById(@PathVariable("userId") String userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ApiResponse.<UserResponse, Void>builder()
                .data(userResponse)
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse getMyInfo(){
        UserResponse userResponse = userService.getMyInfo();
        return ApiResponse.<UserResponse, Void>builder()
                .data(userResponse)
                .build();
    }

    @PostMapping()
    ApiResponse<UserResponse, Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ApiResponse.<UserResponse, Void>builder()
                .data(userResponse)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse, Void> updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse, Void>builder()
                .data(userResponse)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String, Void> deleteUserById(@PathVariable("userId") String userId) {
        ApiResponse<String, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.deleteUser(userId));
        return apiResponse;
    }
}
