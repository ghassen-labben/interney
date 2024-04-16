package com.example.recruitment.user;


import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    public void saveUser(User user) {
        User user1=repository.findByUsername(user.getUsername());
        user1.setStatus(Boolean.TRUE);
        repository.save(user1);
    }

    public void disconnect(User user) {
        var storedUser = repository.findByUsername(user.getUsername());
        if (storedUser != null) {
            storedUser.setStatus(Boolean.FALSE);
            repository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Boolean.TRUE);
    }
}
