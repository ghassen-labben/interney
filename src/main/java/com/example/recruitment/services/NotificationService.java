package com.example.recruitment.services;

import com.example.recruitment.models.EmailDetails;
import com.example.recruitment.models.Notification;
import com.example.recruitment.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NotificationService(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());

    }


    public void sendNotification(Notification notification) {


        Message<Notification> message = MessageBuilder
                .withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, "notifications")
                .setHeader("__TypeId__", Notification.class.getName())
                .build();

        for (User user:notification.getUsers()
             ) {
            String email=user.getEmail();
            System.out.println(email);
            EmailDetails emailDetails = new EmailDetails(email, notification.getMessage(),"neew offers SIGA");
            kafkaTemplate.send("email-topic", emailDetails);

        }

        kafkaTemplate.send(message);
    }
}