package com.example.recruitment.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConfig {

    @Bean
    public <T> ErrorHandlingDeserializer<T> errorHandlingDeserializer() {
        Deserializer<T> deserializer = new JsonDeserializer<>(Object.class);
        return new ErrorHandlingDeserializer<>(deserializer);
    }
}
