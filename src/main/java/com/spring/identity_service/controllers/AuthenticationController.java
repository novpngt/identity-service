package com.spring.identity_service.controllers;

import com.nimbusds.jose.JOSEException;
import com.spring.identity_service.DTOs.requests.AuthenticationRequest;
import com.spring.identity_service.DTOs.requests.IntrospectRequest;
import com.spring.identity_service.DTOs.requests.LogoutRequest;
import com.spring.identity_service.DTOs.requests.RefreshTokenRequest;
import com.spring.identity_service.DTOs.responses.ApiResponse;
import com.spring.identity_service.DTOs.responses.AuthenticationResponse;
import com.spring.identity_service.DTOs.responses.IntrospectResponse;
import com.spring.identity_service.DTOs.responses.LogoutResponse;
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

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse, Void> authenticate(@RequestBody final AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse, Void>builder().
                data(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse, Void> introspect(@RequestBody final IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse, Void>builder()
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse logout(@RequestBody final LogoutRequest request) throws ParseException, JOSEException {
        LogoutResponse result = authenticationService.logout(request);
        return ApiResponse.builder()
                .data(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse, Void> refresh(@RequestBody final RefreshTokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse, Void>builder()
                .data(authenticationService.refreshToken(request))
                .build();
    }
}
