package com.spring.identity_service.services;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;

    private UserCreateRequest userRequest;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    public void initData() {
        userRequest = UserCreateRequest.builder()
                .username("user")
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
    }

    @Test
    void createUser_validRequest_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);
        //WHEN
        var response = userService.createUser(userRequest);
        //THEN
        Assertions.assertEquals(userResponse.getUsername(), response.getUsername());
    }

    @Test
    void createUser_userExisted_fail() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);
        //WHEN
        var exception = Assertions.assertThrows(AppException.class, ()->userService.createUser(userRequest));
        //THEN
        Assertions.assertEquals(exception.getErrorCode().getMessage(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.USER_ALREADY_EXISTS.getCode());
    }
}
