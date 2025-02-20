package com.spring.identity_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS(1001, "User already exists", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_ERROR(1002, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_VALIDATION_ERROR(1003, "User validation error", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTS(1005, "User not exists", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED_ERROR(1006, "Unauthenticated error", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN(1007, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
    JWT_VERIFICATION_FAILED(1008, "JWT verification failed", HttpStatus.UNAUTHORIZED),
    UNREADABLE_MESSAGE(1009, "Http message is not readable", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ERROR(1010, "You do not have this permission", HttpStatus.FORBIDDEN),

    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
