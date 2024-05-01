package com.example.recruitment.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200" )
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        try {
            ChatMessage savedMsg = chatMessageService.savechat(chatMessage);

            // Send notification to the recipient's queue
            String destinationForRecipient = "/queue/messages";
            ChatNotification notificationForRecipient = new ChatNotification(
                    savedMsg.getSender(), savedMsg.getRecipient(), savedMsg.getContent());
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipient().getUsername(), destinationForRecipient, notificationForRecipient);

            String destinationForSender = "/queue/messages";
            ChatNotification notificationForSender = new ChatNotification(
                    savedMsg.getSender(), savedMsg.getRecipient(), savedMsg.getContent());
            notificationForSender.setSeen(true);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getSender().getUsername(), destinationForSender, notificationForSender);
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.addAll(chatMessageService.findChatMessages(senderId, recipientId));
        messages.addAll(chatMessageService.findChatMessages(recipientId, senderId));
        messages.sort(Comparator.comparing(ChatMessage::getTimestamp).reversed());
        return ResponseEntity.ok(messages);
    }
}