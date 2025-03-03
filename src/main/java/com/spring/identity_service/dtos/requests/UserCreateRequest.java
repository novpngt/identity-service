package com.spring.identity_service.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.spring.identity_service.validators.DOBConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 6, max = 20, message = "INVALID_USERNAME")
    String username;

    @Size(min = 6, max = 32, message = "INVALID_PASSWORD")
    String password;

    @NotEmpty(message = "INVALID_FIRSTNAME")
    String firstName;

    @NotEmpty(message = "INVALID_LASTNAME")
    String lastName;

    @DOBConstraint(min = 18, message = "INVALID_AGE")
    LocalDate birthDate;
}
