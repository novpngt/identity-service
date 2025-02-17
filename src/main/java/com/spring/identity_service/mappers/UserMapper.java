package com.spring.identity_service.mappers;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.requests.UserUpdateRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponses(List<User> users);
}
