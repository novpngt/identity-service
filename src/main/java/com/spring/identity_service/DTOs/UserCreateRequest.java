package com.spring.identity_service.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserCreateRequest {
    @Size(min = 6, max = 20,
            message = "Username must be between {min} and {max} characters long.")
    private String username;
    @Size(min=6, max=32,
            message = "Password must be between {min} and {max} characters long.")
    private String password;
    @NotEmpty(message = "Please provide your first name. It cannot be left blank.")
    private String firstName;
    @NotEmpty(message = "Please provide your last name. It cannot be left blank.")
    private String lastName;

    private LocalDate birthDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
