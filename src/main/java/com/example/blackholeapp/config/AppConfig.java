package com.example.blackholeapp.config;

// Import necessary Spring and security classes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is a Configuration class for setting up beans in the application context. ( beans are objects that are managed by the Spring frameworks)
 * This class provides configuration related to password encryption.
 */
@Configuration
public class AppConfig {

    /**
     * Defines a bean for PasswordEncoder that uses BCryptPasswordEncoder.
     * BCryptPasswordEncoder is a secure way to hash passwords for storage.
     *
     * @return PasswordEncoder - a BCryptPasswordEncoder instance for encoding passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
