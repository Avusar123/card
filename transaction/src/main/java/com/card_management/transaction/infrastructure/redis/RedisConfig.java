package com.card_management.transaction.infrastructure.redis;

import com.card_management.transaction.infrastructure.saga.TransactionDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, TransactionDocument> documentRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TransactionDocument> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        var mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.registerModule(new Jdk8Module());

        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<TransactionDocument> jsonSerializer = new Jackson2JsonRedisSerializer<>(mapper, TransactionDocument.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();

        return template;
    }
}
