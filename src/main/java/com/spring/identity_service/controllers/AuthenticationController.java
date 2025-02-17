package com.spring.identity_service.controllers;

import com.spring.identity_service.DTOs.requests.AuthenticationRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.AuthenticationResponse;
import com.spring.identity_service.services.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse authenticate(@RequestBody final AuthenticationRequest request) {

        boolean result = authenticationService.authenticate(request);

        return ApiResponse.builder().
                data(AuthenticationResponse.builder()
                        .authenticated(result)
                        .build())
                .build();
    }
}
