package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.requests.RoleRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.PermissionResponse;
import com.spring.identity_service.DTOs.responses.RoleResponse;
import com.spring.identity_service.services.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping()
    ApiResponse<RoleResponse, Void> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse, Void>builder()
                .data(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleResponse>, Void> getRoles() {
        return ApiResponse.<List<RoleResponse>, Void>builder()
                .data(roleService.getRoles())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<String, Void> delete(@PathVariable String roleId) {
        return ApiResponse.<String, Void>builder()
                .data(roleService.delete(roleId))
                .build();
    }
}
