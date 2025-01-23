package com.spring.identity_service.exceptions;

public enum ErrorCode {
    USER_ALREADY_EXISTS(1001, "User already exists"),
    UNCATEGORIZED_ERROR(1002, "Uncategorized error"),
    USER_VALIDATION_ERROR(1003, "User validation error"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
