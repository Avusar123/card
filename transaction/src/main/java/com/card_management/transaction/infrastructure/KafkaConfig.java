package com.card_management.transaction.infrastructure;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public NewTopic transaction() {
        return new NewTopic("transaction-saga", 1, (short) 1);
    }

    @Bean
    public NewTopic transactionResponse() {
        return new NewTopic("transaction-saga-response", 1, (short) 1);
    }
}
