package com.spring.identity_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.spring.identity_service.services.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("${jwt.signer-key}")
    String SIGNER_KEY;

    AuthenticationService authenticationService;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        return authenticationService.verifyToken(token, false);
        //        try {
        //            authenticationService.introspect(IntrospectRequest.builder()
        //                    .token(token)
        //                    .build());
        //        } catch (ParseException | JOSEException e) {
        //            throw new JwtException(e.getMessage());
        //        }
        //        if(Objects.isNull(nimbusJwtDecoder)){
        //            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        //            nimbusJwtDecoder = NimbusJwtDecoder
        //                .withSecretKey(secretKeySpec)
        //                .macAlgorithm(MacAlgorithm.HS512)
        //                .build();
        //        }
        //        return nimbusJwtDecoder.decode(token);
    }
}
