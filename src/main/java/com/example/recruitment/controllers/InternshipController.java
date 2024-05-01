package com.example.recruitment.controllers;

import com.example.recruitment.models.*;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.InternshipService;
import com.example.recruitment.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

// InternshipController.java
@RestController
@RequestMapping(value="/api/internships" )
public class InternshipController {

    private final InternshipService internshipService;

    private final NotificationService notificationService;

    private final CustomUserDetailsService userService;


    @Autowired
    public InternshipController(InternshipService internshipService, NotificationService notificationService, CustomUserDetailsService userService) {
        this.internshipService = internshipService;
        this.notificationService = notificationService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public List<Internship> getAll()
    {
        return  this.internshipService.getAllInternships();
    }
    @GetMapping
    public ResponseEntity<PagedResponse<Internship>> getAllInternships(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String department) {
        PagedResponse<Internship> response;

        if (skills != null && !skills.isEmpty() && searchQuery != null && !searchQuery.isEmpty() && department != null && !department.isEmpty()) {
            response = internshipService.getInternshipsBySkillsQueryAndDepartment(page, size, skills, searchQuery, department);
        } else if (skills != null && !skills.isEmpty() && department != null && !department.isEmpty()) {
            response = internshipService.getInternshipsBySkillsAndDepartment(page, size, skills, department);
        } else if (searchQuery != null && !searchQuery.isEmpty() && department != null && !department.isEmpty()) {
            response = internshipService.getInternshipsByQueryAndDepartment(page, size, searchQuery, department);
        } else if (skills != null && !skills.isEmpty() && searchQuery != null && !searchQuery.isEmpty()) {
            response = internshipService.getInternshipsBySkillsAndQuery(page, size, skills, searchQuery);
        } else if (skills != null && !skills.isEmpty()) {
            response = internshipService.getInternshipsBySkills(page, size, skills);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            response = internshipService.getInternshipsByQuery(page, size, searchQuery);
        } else if (department != null && !department.isEmpty()) {
            response = internshipService.getInternshipsByDepartment(page, size, department);
        } else {
            response = internshipService.getInternshipsByPage(page, size);
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/hasApplicants")
    public ResponseEntity<PagedResponse<Internship>> getAllInternshipsWithApplicants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<Internship> response;


        response = internshipService.getInternshipsWithApplicant(page, size);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/department/{departmentId}")
    public ResponseEntity<PagedResponse<Internship>> getInternshipsByDepartment(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String searchQuery, @PathVariable String departmentId){
        PagedResponse<Internship> response;
        response = internshipService.getInternshipsByDeparmtentAndSearch(page, size,departmentId,searchQuery);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Internship> getInternshipById(@PathVariable Long id) {
        Optional<Internship> internship = internshipService.getInternshipById(id);
        return internship.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @PostMapping
    public ResponseEntity<Internship> saveInternship(@RequestBody Internship internship) {
        try {
            Internship savedInternship = internshipService.saveInternship(internship);
            List<User> users = userService.findByAuthority("ROLE_USER");
            System.out.println(users);

            String notificationType = "new-internship";
            String message = "A new internship opportunity has been posted: http://localhost:4200/internship/" + savedInternship.getId();
            Notification notification = new Notification(new HashSet<>(users), notificationType, message);

            notificationService.sendNotification(notification);

            return new ResponseEntity<>(savedInternship, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Internship> updateInternship(@PathVariable Long id, @RequestBody Internship internship) {
        try {
            Internship updatedInternship = internshipService.updateInternship(id, internship);
            return new ResponseEntity<>(updatedInternship, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }





    @GetMapping("/top10")
    public List<Internship> getTop10()
    {
        return internshipService.getTop10();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternship(@PathVariable Long id) {
        internshipService.deleteInternship(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
