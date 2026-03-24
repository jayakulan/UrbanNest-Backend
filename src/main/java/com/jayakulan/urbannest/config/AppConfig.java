package com.jayakulan.urbannest.config;

// AppConfig is intentionally left empty.
// PasswordEncoder bean has been moved to SecurityConfig to co-locate
// all security-related beans and avoid circular dependency issues.

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // PasswordEncoder is now in SecurityConfig
}
