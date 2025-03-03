package com.spring.identity_service.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {
    USER_ALREADY_EXISTS(1001, "User already exists", HttpStatus.CONFLICT),
    UNCATEGORIZED_ERROR(1002, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_VALIDATION_ERROR(1003, "User validation error", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTS(1005, "User not exists", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED_ERROR(1006, "Unauthenticated error", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN(1007, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
    JWT_VERIFICATION_FAILED(1008, "JWT verification failed", HttpStatus.UNAUTHORIZED),
    UNREADABLE_MESSAGE(1009, "Http message is not readable", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ERROR(1010, "You do not have this permission", HttpStatus.FORBIDDEN),
    PERMISSION_ALREADY_EXISTS(1011, "Permission already exists", HttpStatus.CONFLICT),
    ROLE_ALREADY_EXISTS(1012, "Role already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(1013, "Role not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME(1014, "Username must be between {min} and {max} characters long.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1015, "Password must be between {min} and {max} characters long.", HttpStatus.BAD_REQUEST),
    INVALID_FIRST_NAME(1016, "Please provide your first name. It cannot be left blank.", HttpStatus.BAD_REQUEST),
    INVALID_LAST_NAME(1017, "Please provide your last name. It cannot be left blank.", HttpStatus.BAD_REQUEST),
    INVALID_AGE(1018, "Minimum age must be greater than or equal to {min}", HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS(1019, "Bad credentials", HttpStatus.UNAUTHORIZED),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
