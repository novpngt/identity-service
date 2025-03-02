package com.spring.identity_service.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.Role;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.repositories.RoleRepository;
import com.spring.identity_service.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    private UserCreateRequest userRequest;
    private UserResponse userResponse;
    private User user;
    private Role role;

    @BeforeEach
    public void initData() {
        userRequest = UserCreateRequest.builder()
                .username("user05")
                .password("123456")
                .lastName("userLN5")
                .firstName("userFN5")
                .birthDate(LocalDate.of(2000, 2, 2))
                .build();

        userResponse = UserResponse.builder()
                .id("user-UUID-format")
                .username("user05")
                .lastName("userLN5")
                .firstName("userFN5")
                .birthDate(LocalDate.of(2000, 2, 2))
                .build();

        user = User.builder()
                .password("hashed-chain-password")
                .username("user05")
                .lastName("userLN5")
                .firstName("userFN5")
                .birthDate(LocalDate.of(2000, 2, 2))
                .build();

        role = Role.builder().name("USER").description("USER ROLE").build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(anyString())).thenReturn(Optional.ofNullable(role));
        // WHEN
        var response = userService.createUser(userRequest);
        // THEN
        Assertions.assertEquals(userResponse.getUsername(), response.getUsername());
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);
        // WHEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.createUser(userRequest));
        // THEN
        Assertions.assertEquals(exception.getErrorCode().getMessage(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.USER_ALREADY_EXISTS.getCode());
    }

    @Test
    @WithMockUser(username = "user")
    void getInfo_validRequest_success() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        Assertions.assertEquals(userResponse.getUsername(), user.getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void getInfo_userNotFound_fail() {
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        var exception = Assertions.assertThrows(AppException.class, () -> userService.getMyInfo());
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.USER_NOT_FOUND.getCode());
        Assertions.assertEquals(exception.getErrorCode().getMessage(), ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
