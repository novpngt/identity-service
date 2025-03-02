package com.spring.identity_service.services;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.spring.identity_service.DTOs.requests.AuthenticationRequest;
import com.spring.identity_service.DTOs.requests.IntrospectRequest;
import com.spring.identity_service.DTOs.requests.LogoutRequest;
import com.spring.identity_service.DTOs.requests.RefreshTokenRequest;
import com.spring.identity_service.DTOs.responses.AuthenticationResponse;
import com.spring.identity_service.DTOs.responses.IntrospectResponse;
import com.spring.identity_service.DTOs.responses.LogoutResponse;
import com.spring.identity_service.entities.InvalidatedToken;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.enums.ErrorCode;
import com.spring.identity_service.exceptions.AppException;
import com.spring.identity_service.repositories.InvalidatedTokenRepository;
import com.spring.identity_service.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.token-ttl}")
    protected int TOKEN_TTL;

    @NonFinal
    @Value("${jwt.token-grace-period}")
    protected int TOKEN_GRACE_PERIOD;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public LogoutResponse logout(LogoutRequest request) throws JwtException {
        Jwt jwt = verifyToken(request.getToken(), true); // Trả về Jwt
        String jit = jwt.getId(); // Lấy jti
        Date expiryTime = Date.from(jwt.getExpiresAt()); // Chuyển Instant sang Date
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiration(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
        return LogoutResponse.builder().success(true).build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws JwtException {
        Jwt jwt = verifyToken(request.getToken(), true); // Trả về Jwt
        String jit = jwt.getId(); // Lấy jti
        Date expiryTime = Date.from(jwt.getExpiresAt()); // Chuyển Instant sang Date
        User user = userRepository
                .findByUsername(jwt.getSubject()) // Lấy sub
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String token = generateToken(user);
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiration(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public Jwt verifyToken(String token, boolean isRefresh) throws JwtException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

        Jwt jwt;
        try {
            jwt = decoder.decode(token);
        } catch (JwtException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
        }

        Instant expiration =
                isRefresh ? jwt.getIssuedAt().plus(TOKEN_GRACE_PERIOD, ChronoUnit.SECONDS) : jwt.getExpiresAt();
        if (expiration.isBefore(Instant.now())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
        }

        String jwtId = jwt.getId();
        if (jwtId != null && invalidatedTokenRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
        }

        return jwt;
    }

    /*
    public LogoutResponse logout(LogoutRequest request) throws ParseException, JOSEException {
    		var signedToken = verifyToken(request.getToken(), true);
    		String jit = signedToken.getJWTClaimsSet().getJWTID();
    		Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
    		InvalidatedToken invalidatedToken = InvalidatedToken.builder()
    				.id(jit)
    				.expiration(expiryTime)
    				.build();
    		invalidatedTokenRepository.save(invalidatedToken);
    		return LogoutResponse.builder()
    				.success(true)
    				.build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
    	var signToken = verifyToken(request.getToken(), true);
    	String jit = signToken.getJWTClaimsSet().getJWTID();
    	Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

    	User user = userRepository.findByUsername(signToken.getJWTClaimsSet().getSubject())
    			.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    	String token = generateToken(user);

    	InvalidatedToken invalidatedToken = InvalidatedToken.builder()
    			.id(jit)
    			.expiration(expiryTime)
    			.build();

    	invalidatedTokenRepository.save(invalidatedToken);

    	return AuthenticationResponse.builder()
    			.token(token)
    			.authenticated(true)
    			.build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
    	JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
    	SignedJWT signedJWT = SignedJWT.parse(token);
    	Date expiration;
    	if (isRefresh) {
    		expiration = new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
    				.plus(TOKEN_GRACE_PERIOD,ChronoUnit.SECONDS).toEpochMilli());
    	} else {
    		expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
    	}
    	var verified = signedJWT.verify(verifier);
    	if (!(verified && expiration.after(new Date()))) {
    		throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
    	}
    	boolean isLoggedOut = invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
    	if (isLoggedOut) {
    		throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
    	}
    	return signedJWT;
    }
     */

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        verifyToken(token, false);
        return IntrospectResponse.builder().valid(true).build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("identity-service.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(TOKEN_TTL, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }
}
