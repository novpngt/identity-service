package com.spring.identity_service.services;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.identity_service.dtos.requests.RoleRequest;
import com.spring.identity_service.dtos.responses.RoleResponse;
import com.spring.identity_service.entities.Role;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.mappers.RoleMapper;
import com.spring.identity_service.repositories.PermissionRepository;
import com.spring.identity_service.repositories.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getRoles() {
        var roles = roleRepository.findAll();
        return roles.stream().map(role -> roleMapper.toRoleResponse(role)).collect(Collectors.toList());
    }

    public String delete(String name) {
        if (!roleRepository.existsByName(name)) {
            return "Failed";
        }
        roleRepository.deleteById(name);
        return "Success";
    }
}
