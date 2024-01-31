package com.example.recruitment.controllers;

import com.example.recruitment.models.Internship;
import com.example.recruitment.services.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// InternshipController.java
@RestController
@RequestMapping("/internships")
@CrossOrigin("http://localhost:4200/")
public class InternshipController {

    private final InternshipService internshipService;

    @Autowired
    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping
    public ResponseEntity<List<Internship>> getAllInternships() {
        List<Internship> internships = internshipService.getAllInternships();
        return new ResponseEntity<>(internships, HttpStatus.OK);
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
/*
    @PutMapping("/{id}")
    public ResponseEntity<Internship> updateInternship(@PathVariable Long id, @RequestBody Internship internship) {
        try {
            Internship updatedInternship = internshipService.updateInternship(id, internship);
            return new ResponseEntity<>(updatedInternship, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
*/
@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternship(@PathVariable Long id) {
        internshipService.deleteInternship(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
