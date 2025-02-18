package com.spring.identity_service.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <DataType, ErrorType>{
    int code;
    @Builder.Default
    String message = "success";
    DataType data;
    ErrorType errors;
}
