package com.example.recruitment.controllers;

import com.example.recruitment.models.Education;
import com.example.recruitment.models.Profile;
import com.example.recruitment.services.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/educations")
public class EducationController {
    private final EducationService educationService;

    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public ResponseEntity<List<Education>> getAllEducations() {
        List<Education> educations = educationService.getAllEducations();
        return new ResponseEntity<>(educations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable Long id) {
        Optional<Education> education = educationService.getEducationById(id);
        return education.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



}
