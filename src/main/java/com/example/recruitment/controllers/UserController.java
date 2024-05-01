package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.*;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.DepartmentService;
import com.example.recruitment.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Utils jwtTokenUtil;

    @Autowired
     private  DepartmentService departmentService;

    @Autowired
    private AttachmentController attachmentController;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserController( KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @GetMapping
    public ResponseEntity<Set<User>> getAllUsers(HttpServletRequest request) {
        Set<User> users = userService.findConnectedUsers(request);
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

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(@Payload User user, @Header("simpSessionId") String sessionId) {
        User user2 = userService.getUserByUsername(user.getUsername());
        user2.setStatus(true);
        userService.saveUser(user2);

        return user2;
    }


    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        user.setStatus(false);
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/currentUser")
    public ResponseEntity<User> currentUser(HttpServletRequest request)
    {
        String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user=userRepository.findByUsername(username);
        return  ResponseEntity.ok(user);
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
        if(user.getDepartment()!=null && user.getDepartment().getName()!=null)
        {   Department department=user.getDepartment();
            department.addEncadrant(user);
            departmentService.saveDepartment(department);
        }
        User newUser = userRepository.save(user);
        if (user.getCreatedBy() != null && user.getCreatedBy().equals("admin")) {
            EmailDetails emailDetails = new EmailDetails(email, "email : " + email + " | password : " + password, "your account info");
            kafkaTemplate.send("email-topic", emailDetails);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }
    @GetMapping("/{userId}/profileImage")
    public  ResponseEntity<Resource> getProfilePicture(@PathVariable Long userId)
    {
        Optional<User> user=userRepository.findById(userId);
        if(user.isEmpty())
            return null;
      return this.attachmentController.downloadAttachment(user.get().getProfile().getProfileImage().getId());

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

    @GetMapping("/encadrants/{departmentId}")
    public ResponseEntity<Set<User>> getEncadrants(@PathVariable String departmentId) {
        Department department=departmentService.getDepartmentById(departmentId);
        Set<User> encadrants=department.getEncadrants();


        return ResponseEntity.ok(encadrants);
    }

    @GetMapping("/encadrant/numberSupervised/{encadrantId}")
    public ResponseEntity<Object> isEncadrantAvailable(@PathVariable Long encadrantId) {
        Optional<User> userOptional = userRepository.findById(encadrantId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ENCADRANT"))) {
                Long supervisedApplicationsCount = user.getApplicationsSupervisees().stream()
                        .filter(internshipApplication ->internshipApplication.getEncadrantAccepted()==null || internshipApplication.getEncadrantAccepted()==true )
                        .count();
                return ResponseEntity.ok(supervisedApplicationsCount);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User with id " + encadrantId + " is not an Encadrant");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id " + encadrantId + " not found");
        }
    }
}
