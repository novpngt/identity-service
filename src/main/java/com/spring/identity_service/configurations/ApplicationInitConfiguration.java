package com.spring.identity_service.configurations;

import com.spring.identity_service.entities.Role;
import com.spring.identity_service.entities.User;
import com.spring.identity_service.repositories.RoleRepository;
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

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfiguration.class);
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner init() {
        return args -> {
            Role adminRole = roleRepository.findById("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = Role.builder().name("ADMIN").description("ADMIN ROLE").build();
                        return roleRepository.save(newRole);
                    });
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .roles(Set.of(adminRole))
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(admin);
                log.warn("admin user has been created (default password: admin). {}. Please change password immediately!!!", admin);
            }
        };
    }
}
