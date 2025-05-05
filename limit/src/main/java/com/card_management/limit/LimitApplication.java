package com.card_management.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "com.card_management")
@EnableWebSecurity
public class LimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitApplication.class, args);
    }

}
