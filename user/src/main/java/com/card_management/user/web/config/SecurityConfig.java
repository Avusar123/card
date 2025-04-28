package com.card_management.user.web.config;

import com.card_management.web_security.AdditionalSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Bean
    public AdditionalSecurityConfigurer additionalSecurityConfigurer() {
        return auth -> auth
                .requestMatchers("/user/login")
                .permitAll();
    }
}
