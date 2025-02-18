package com.spring.identity_service.services;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.requests.UserUpdateRequest;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.enums.Role;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.mappers.UserMapper;
import com.spring.identity_service.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        return userMapper.toUserResponses(userRepository.findAll());
    }

    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser (String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
        return "deleted";
    }
}
