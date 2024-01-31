package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.Application;
import com.example.recruitment.models.Candidate;
import com.example.recruitment.repositories.CandidateRepository;
import com.example.recruitment.services.ApplicationService;
import com.example.recruitment.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

// ApplicationController.java
@RestController
@RequestMapping("/applications")
@CrossOrigin("http://localhost:4200/")
public class ApplicationController {
    @Autowired
    private Utils jwtTokenUtil;
    private final ApplicationService applicationService;

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }
    @GetMapping("/candidate/{id}")
    public List<Application> getApplicationsByCandidate(@PathVariable Long id) {
        List<Application> applications = applicationService.getApplicationsByCandidate(id);
        return applications;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        return application.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/save")
    public ResponseEntity<Application> saveApplicationWithDetails(
            @RequestParam Long internshipId,
            @RequestParam String attachmentId,
            HttpServletRequest request) {
        String jwt=request.getHeader("Authorization").split(" ")[1];
        try {
String username=jwtTokenUtil.extractUsername(jwt);
          Candidate candidate=candidateRepository.findCandidateByUsername(username);
            Application savedApplication = applicationService.saveApplicationWithDetails(
                    internshipId, candidate.getId(), attachmentId);
            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
