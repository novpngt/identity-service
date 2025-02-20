package com.spring.identity_service.exceptions;

import com.nimbusds.jose.JOSEException;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> runTimeExceptionHandler(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_ERROR.getMessage() + ": " + e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedExceptionHandler() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .message(errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }

    @ExceptionHandler(value = ParseException.class)
    ResponseEntity<ApiResponse> handleParseException(ParseException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_JWT_TOKEN.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_JWT_TOKEN.getMessage() + ": " + e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = JOSEException.class)
    ResponseEntity<ApiResponse> handleJOSEException(JOSEException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.JWT_VERIFICATION_FAILED.getCode());
        apiResponse.setMessage(ErrorCode.JWT_VERIFICATION_FAILED.getMessage() + ": " + e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handleMissingRequestBody(HttpMessageNotReadableException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNREADABLE_MESSAGE.getCode());
        apiResponse.setMessage(ErrorCode.UNREADABLE_MESSAGE.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        //Listing validation errors
        List<ValidationError> userValidationErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.USER_VALIDATION_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.USER_VALIDATION_ERROR.getMessage());
        apiResponse.setErrors(userValidationErrors);

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
