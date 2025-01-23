package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.ApiResponse;
import com.spring.identity_service.DTOs.UserCreateRequest;
import com.spring.identity_service.DTOs.UserUpdateRequest;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    ApiResponse<User, Void> createUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<User, Void> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    User getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    User updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUserById(@PathVariable("userId") String userId) {
        return userService.deleteUser(userId);
    }
}
