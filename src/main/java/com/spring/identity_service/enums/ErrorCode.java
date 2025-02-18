package com.spring.identity_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS(1001, "User already exists"),
    UNCATEGORIZED_ERROR(1002, "Uncategorized error"),
    USER_VALIDATION_ERROR(1003, "User validation error"),
    USER_NOT_FOUND(1004, "User not found"),
    USER_NOT_EXISTS(1005, "User not exists"),
    UNAUTHORIZED_ERROR(1006, "Unauthorized error"),
    INVALID_JWT_TOKEN(1007, "Invalid JWT token"),
    JWT_VERIFICATION_FAILED(1008, "JWT verification failed"),
    UNREADABLE_MESSAGE(1009, "Http message is not readable"),
    ;

    int code;
    String message;
}
