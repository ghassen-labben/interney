package com.example.recruitment.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    @Autowired
    private  ChatMessageRepository repository;

    public ChatMessage savechat(ChatMessage chatMessage) {
        return  repository.save(chatMessage);

    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        return repository.findChatMessagesBySenderUsernameAndRecipientUsernameOrderByTimestamp(senderId,recipientId);

    }
}
