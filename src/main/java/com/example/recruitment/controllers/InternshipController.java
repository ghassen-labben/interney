package com.example.recruitment.controllers;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.SkillRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// InternshipController.java
@RestController
@RequestMapping(value="/api/internships" )
public class InternshipController {

    private final InternshipService internshipService;




    @Autowired
    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }
    @GetMapping
    public ResponseEntity<PagedResponse<Internship>> getAllInternships(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String searchQuery) {
        PagedResponse<Internship> response;


        if (skills != null && !skills.isEmpty() && searchQuery != null && !searchQuery.isEmpty()) {
            response = internshipService.getInternshipsBySkillsAndQuery(page, size, skills, searchQuery);
        } else if (skills != null && !skills.isEmpty()) {
            response = internshipService.getInternshipsBySkills(page, size, skills);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            response = internshipService.getInternshipsByQuery(page, size, searchQuery);
        } else {
            response = internshipService.getInternshipsByPage(page, size);
        }
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
            return new ResponseEntity<>(savedInternship, HttpStatus.CREATED);
        } catch (Exception e) {
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
