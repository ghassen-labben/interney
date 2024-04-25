package com.example.recruitment.user;


import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
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

    public Set<User> findConnectedUsers(HttpServletRequest request){
User user=userDetailsService.getUser(request);
if(user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ENCADRANT") ))
{
    Set<User> candidates = user.getApplicationsSupervisees().stream()
            .filter(application -> application.getEncadrantAccepted())
            .map(InternshipApplication::getCandidate)
            .collect(Collectors.toSet());
return  candidates;
}
        return repository.findEncadrantByCandidate(user);
    }
}
