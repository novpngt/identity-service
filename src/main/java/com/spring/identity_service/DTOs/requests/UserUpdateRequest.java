package com.spring.identity_service.DTOs.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 6, max = 20,
            message = "Username must be between {min} and {max} characters long.")
    String username;

    @Size(min=6, max=32,
            message = "Password must be between {min} and {max} characters long.")
    String password;

    @NotEmpty(message = "Please provide your first name. It cannot be left blank.")
    String firstName;

    @NotEmpty(message = "Please provide your last name. It cannot be left blank.")
    String lastName;

    LocalDate birthDate;
}
