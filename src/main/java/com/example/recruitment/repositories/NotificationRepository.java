package com.example.recruitment.repositories;

import com.example.recruitment.models.Notification;
import com.example.recruitment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUsersContaining(User user);
}
