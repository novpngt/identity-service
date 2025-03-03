package com.spring.identity_service.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.spring.identity_service.dtos.requests.UserCreateRequest;
import com.spring.identity_service.dtos.requests.UserUpdateRequest;
import com.spring.identity_service.dtos.responses.ApiResponse;
import com.spring.identity_service.dtos.responses.UserResponse;
import com.spring.identity_service.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>, Void> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ApiResponse.<List<UserResponse>, Void>builder().data(users).build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse, ?> getUserById(@PathVariable("userId") String userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ApiResponse.<UserResponse, Void>builder().data(userResponse).build();
    }

    @GetMapping("/my-info")
    ApiResponse getMyInfo() {
        UserResponse userResponse = userService.getMyInfo();
        return ApiResponse.<UserResponse, Void>builder().data(userResponse).build();
    }

    @PostMapping()
    ApiResponse<UserResponse, Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        log.info("Controller: createUser");
        UserResponse userResponse = userService.createUser(request);
        System.out.println("UserResponse: " + userResponse);
        return ApiResponse.<UserResponse, Void>builder().data(userResponse).build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse, Void> updateUserById(
            @PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse, Void>builder().data(userResponse).build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String, Void> deleteUserById(@PathVariable("userId") String userId) {
        ApiResponse<String, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.deleteUser(userId));
        return apiResponse;
    }
}
