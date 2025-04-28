package com.card_management.user.infrastructure;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public NewTopic userCheckResultTopic() {
        return new NewTopic("user-check-result", 2, (short) 1);
    }

    @Bean
    public NewTopic userDeletedTopic() {
        return new NewTopic("user-deleted", 1, (short) 1);
    }
}
