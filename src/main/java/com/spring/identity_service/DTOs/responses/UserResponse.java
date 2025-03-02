package com.spring.identity_service.DTOs.responses;

import java.time.LocalDate;
import java.util.Set;

import com.spring.identity_service.entities.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate birthDate;
    Set<Role> roles;
}
