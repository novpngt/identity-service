package com.spring.identity_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.spring.identity_service.dtos.requests.PermissionRequest;
import com.spring.identity_service.dtos.responses.PermissionResponse;
import com.spring.identity_service.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);
}
