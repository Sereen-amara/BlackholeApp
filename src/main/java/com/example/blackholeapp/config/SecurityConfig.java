package com.example.blackholeapp.config;

import com.example.blackholeapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/register", "/users/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/criminals/dashboard").hasAnyAuthority("ROLE_ADMIN", "ROLE_REVIEWER")
                        .requestMatchers("/criminals/add").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login")
                        .defaultSuccessUrl("/criminals/dashboard", true)
                        .failureUrl("/users/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/login")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

// spring boot official documentation
// https://spring.io/projects/spring-boot
// provides comprehensive information on setting up and configuring spring boot applications

// baeldung spring boot tutorials
// https://www.baeldung.com/spring-boot
// step-by-step guides for project setup, dependency management, and application configuration

// spring data jpa reference documentation
// https://spring.io/projects/spring-data-jpa
// detailed instructions for repository creation, query methods, and database integrations

// baeldung spring data jpa
// https://www.baeldung.com/spring-data-jpa-query
// guides on how to use spring data jpa for custom queries and advanced features

// spring security official documentation
// https://spring.io/projects/spring-security
// explains authentication, authorization, and securing applications in detail

// baeldung spring security tutorials
// https://www.baeldung.com/spring-security
// provides practical examples for role-based access control and securing endpoints

// thymeleaf official documentation
// https://www.thymeleaf.org/documentation.html
// explains how to use thymeleaf for dynamic web page creation in spring boot applications

// baeldung thymeleaf integration
// https://www.baeldung.com/thymeleaf-in-spring-mvc
// guides for using thymeleaf with spring boot and creating web pages with dynamic data

// baeldung spring security roles and privileges
// https://www.baeldung.com/spring-security-roles-and-privileges
// explains role-based access control, assigning roles, and managing user permissions

// spring boot testing documentation
// https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing
// detailed examples for writing and running tests in spring boot applications
