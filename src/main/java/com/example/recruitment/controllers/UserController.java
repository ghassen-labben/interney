package com.example.recruitment.controllers;

import com.example.recruitment.models.Authority;
import com.example.recruitment.models.EmailDetails;
import com.example.recruitment.models.Skill;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.AuthorityRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.EmailService;
import com.example.recruitment.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;



    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/employers")
    public ResponseEntity<List<User>> getEmployers() {
        List<User> users = userRepository.findAll();
        List<User> employers = users.stream()
                .filter(user -> user.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_STAGIAIRE")
                                || authority.getAuthority().equals("ROLE_ENCADRANT")
                                || authority.getAuthority().equals("ROLE_DIRECTEUR")
                                || authority.getAuthority().equals("ROLE_RH")
                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(employers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/usernames")
    public ResponseEntity<String[]> getUsenames()
    {
        return ResponseEntity.ok(userRepository.findAllUsername());
    }
    @GetMapping("/emails")
    public ResponseEntity<String[]> getEmails()
    {
        return ResponseEntity.ok(userRepository.findAllEmail());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String email=user.getEmail();

        String password=user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));

        if(user.getCreatedBy().equals("admin"))
        {
            EmailDetails emailDetails=new EmailDetails(email,"email : "+email+" | password : "+password,"yoour account info");
            emailService.sendSimpleMail(emailDetails);
        }
        User newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
