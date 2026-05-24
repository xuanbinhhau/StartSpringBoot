package com.example.demo.configuration;

import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {

        return args -> {

            Role role = roleRepository.findById("ADMIN")
                .orElseThrow(() ->
                        new AppException(ErrorCode.UNCASEGOIZE)
                );

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(roles)
                    .build();

                userRepository.save(user);

            log.warn("Admin user has been created with default password: admin");
        } else {

            User user = userRepository.findByUsername("admin")
                    .orElseThrow(() ->
                            new AppException(ErrorCode.UNCASEGOIZE)
                    );

            user.setRoles(roles);

            userRepository.save(user);

            log.info("Add role ADMIN successfully");
        }
    };
}
}
