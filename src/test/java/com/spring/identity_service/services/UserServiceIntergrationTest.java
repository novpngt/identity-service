package com.spring.identity_service.services;

import com.spring.identity_service.DTOs.requests.UserCreateRequest;
import com.spring.identity_service.DTOs.responses.UserResponse;
import com.spring.identity_service.entities.Role;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceIntergrationTest {
    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest");
    @DynamicPropertySource
    static void configDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
    @Transactional
    void createUser_validRequest_success() {
        //GIVEN
        //WHEN
        var response = userService.createUser(userRequest);
        //THEN
        Assertions.assertEquals(userResponse.getUsername(), response.getUsername());
    }

    @Test
    @Transactional
    void createUser_userExisted_fail() {
        //GIVEN
        User existingUser = User.builder()
                .username(userRequest.getUsername())
                .password("hashed-password")
                .lastName("userLN")
                .firstName("userFN")
                .birthDate(LocalDate.of(2000, 2, 2))
                .build();
        userRepository.save(existingUser);
        //WHEN
        var exception = Assertions.assertThrows(AppException.class, ()->userService.createUser(userRequest));
        //THEN
        Assertions.assertEquals(exception.getErrorCode().getMessage(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.USER_ALREADY_EXISTS.getCode());
    }

    @Test
    @WithMockUser(username = "user")
    void getInfo_validRequest_success() {
        Assertions.assertEquals(userResponse.getUsername(), user.getUsername());
    }

    @Test
    @WithMockUser(username = "user")
    void getInfo_userNotFound_fail() {
        var exception = Assertions.assertThrows(AppException.class, ()-> userService.getMyInfo());
        Assertions.assertEquals(exception.getErrorCode().getCode(), ErrorCode.USER_NOT_FOUND.getCode());
        Assertions.assertEquals(exception.getErrorCode().getMessage(), ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
