package com.spring.identity_service.configurations;

import com.spring.identity_service.entities.User;
import com.spring.identity_service.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfiguration.class);
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("@!admin"))
                        .build();
                userRepository.save(admin);
                log.warn("admin user has been created (default password: @!admin). {}. Please change password immediately!!!", admin);
            }
        };
    }
}
