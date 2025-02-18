package com.spring.identity_service.controllers;

import com.nimbusds.jose.JOSEException;
import com.spring.identity_service.DTOs.requests.AuthenticationRequest;
import com.spring.identity_service.DTOs.requests.IntrospectRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.services.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse authenticate(@RequestBody final AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.builder().
                data(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse introspect(@RequestBody final IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.builder()
                .data(result)
                .build();
    }
}
