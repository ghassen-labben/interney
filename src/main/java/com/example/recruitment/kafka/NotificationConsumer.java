package com.example.recruitment.kafka;

import com.example.recruitment.models.Notification;
import com.example.recruitment.models.User;
import com.example.recruitment.services.NotificationService;
import com.example.recruitment.user.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationConsumer {
    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationConsumer(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notifications", groupId = "notifications")
    public void consumeNotification(Notification notification) {
        // Handle the notification
        System.out.println("Received notification: " + notification);
    }
    private void handleNewInternshipNotification(Notification notification) {
        String message = notification.getMessage();

    }

    private void sendNotificationToUser(User user, String message) {
        // Implement your logic to send the notification to the user
        // (e.g., send an email, push notification, etc.)
        System.out.println("Sending notification to " + user.getEmail() + ": " + message);
    }

}
