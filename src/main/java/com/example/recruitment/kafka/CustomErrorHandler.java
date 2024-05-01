package com.example.recruitment.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.core.CoreConstants.MAX_ERROR_COUNT;

@Component
public class CustomErrorHandler implements ErrorHandler {
    private final Map<String, Integer> errorCountMap = new HashMap<>();

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
        String errorKey = thrownException.getMessage(); // Or use a unique identifier from the record
        int errorCount = errorCountMap.getOrDefault(errorKey, 0);

        if (errorCount < MAX_ERROR_COUNT) {
            // Log the error
         errorCountMap.put(errorKey, errorCount + 1);
        } else {
            // Additional actions to prevent infinite loop (e.g., skip further processing)
        }
    }
}