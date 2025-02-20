package com.spring.identity_service.services;

import com.spring.identity_service.DTOs.requests.PermissionRequest;
import com.spring.identity_service.DTOs.responses.PermissionResponse;
import com.spring.identity_service.entities.Permission;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.mappers.PermissionMapper;
import com.spring.identity_service.repositories.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){

        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
        }

        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse( permissionRepository.save(permission));
    }

    public List<PermissionResponse> getPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> permissionMapper.toPermissionResponse(permission))
                .toList();
    }

    public String delete(String name) {
        if (!permissionRepository.existsByName(name)) {
            return "Failed";
        }
        permissionRepository.deleteById(name);
        return "Success";
    }
}
