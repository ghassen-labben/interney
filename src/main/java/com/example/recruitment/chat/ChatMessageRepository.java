package com.example.recruitment.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesBySenderUsernameAndRecipientUsernameOrderByTimestamp(String senderId,String RecipientId);
}
