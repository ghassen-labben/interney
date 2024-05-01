package com.example.recruitment.kafka;

import com.example.recruitment.models.EmailDetails;
import com.example.recruitment.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consumeEmailMessage(EmailDetails emailDetails) {
        emailService.sendSimpleMail(emailDetails);
    }
}