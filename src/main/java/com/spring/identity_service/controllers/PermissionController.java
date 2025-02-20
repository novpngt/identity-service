package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.requests.PermissionRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.PermissionResponse;
import com.spring.identity_service.services.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionResponse, Void> addPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse, Void>builder()
                .data(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>, Void> getPermissions() {
        return ApiResponse.<List<PermissionResponse>, Void>builder()
                .data(permissionService.getPermissions())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<String, Void> delete(@PathVariable String permissionId) {
        return ApiResponse.<String, Void>builder()
                .data(permissionService.delete(permissionId))
                .build();
    }
}
