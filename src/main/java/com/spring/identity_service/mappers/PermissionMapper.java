package com.spring.identity_service.mappers;

import com.spring.identity_service.DTOs.requests.PermissionRequest;
import com.spring.identity_service.DTOs.responses.PermissionResponse;
import com.spring.identity_service.entities.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);
}
