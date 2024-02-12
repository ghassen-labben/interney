package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.*;
import com.example.recruitment.repositories.CandidateRepository;
import com.example.recruitment.services.ApplicationService;
import com.example.recruitment.services.CandidateService;
import com.example.recruitment.services.EmailService;
import com.example.recruitment.services.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
    private EmailService emailService;

    @Autowired
    private InternshipService internshipService;
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
    @PutMapping("/{internshipId}/{candidateId}")
    public ResponseEntity<String> updateApplicationStatus(@PathVariable Long internshipId,
                                                          @PathVariable Long candidateId,
                                                          @RequestParam ApplicationStatus newStatus) {
        try {
            if (!Arrays.asList(ApplicationStatus.values()).contains(newStatus)) {
                return ResponseEntity.badRequest().body("Invalid application status: " + newStatus);
            }

            applicationService.updateApplicationStatus(internshipId, candidateId, newStatus);

            Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
            if (candidate == null) {
                return ResponseEntity.badRequest().body("Candidate not found with ID: " + candidateId);
            }

            Internship internship = internshipService.getInternshipById(internshipId).orElse(null);
            if (internship == null) {
                return ResponseEntity.badRequest().body("Internship not found with ID: " + internshipId);
            }
            String candidateEmail = candidate.getEmail();
            String internshipName = internship.getTitle();
            String companyName = "SIGA";
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(candidateEmail);
            emailDetails.setSubject("Application Status Update - " + companyName);

            String message="";
            switch (newStatus) {
                case ACCEPTED:
                    message = "Dear " + candidate.getUsername() + ",\n\n" +
                            "Congratulations! We are pleased to inform you that your application for the http://localhost:4200/internship/" + internshipId+ " "+
                            internshipName + " internship at " + companyName + " has been accepted.\n\n" +
                            "We look forward to welcoming you to our team!\n\n" +
                            "Best regards,\n " +
                            companyName + " Team";
                    break;
                case REFUSED:
                    message = "Dear " + candidate.getUsername() + ",\n\n" +
                            "We regret to inform you that your application for the http://localhost:4200/internship/" + internshipId+ " "+
                            internshipName + " internship at " + companyName + " has been declined.\n\n" +
                            "Thank you for your interest in our company.\n\n" +
                            "Best regards,\n" +
                            companyName + " Team";
                    break;
                case WAITING_LIST:
                    message = "Dear " + candidate.getUsername() + ",\n\n" +
                            "We would like to inform you that your application for the http://localhost:4200/internship" + internshipId+ " "+
                            internshipName + " internship at " + companyName + " has been placed on a waiting list.\n\n" +
                            "We will notify you if a spot becomes available.\n\n" +
                            "Best regards,\n " +
                            companyName + "Team";
                    break;
            }

            emailDetails.setMsgBody(message);
            try {
                emailService.sendSimpleMail(emailDetails);
            } catch (Exception e) {

                return ResponseEntity.badRequest().body("Error sending email notification: " + e.getMessage());
            }

            return ResponseEntity.ok("Application status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating application status: " + e.getMessage());
        }
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveApplicationWithDetails(
            @RequestParam Long internshipId,
            @RequestParam String attachmentId,
            HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").split(" ")[1];
        try {
            String username = jwtTokenUtil.extractUsername(jwt);
            Candidate candidate = candidateRepository.findCandidateByUsername(username);
            Optional<Internship> internshipOptional = internshipService.getInternshipById(internshipId);
            if (internshipOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Internship not found.");
            }
            Internship internship = internshipOptional.get();
            Application savedApplication = applicationService.saveApplicationWithDetails(
                    internship, candidate, attachmentId); // Pass Internship object
            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

 @GetMapping("/check-application")
 public ResponseEntity<Boolean> check(@RequestParam Long internshipId, HttpServletRequest request)
 {
     String jwt = request.getHeader("Authorization").split(" ")[1];
     String username=jwtTokenUtil.extractUsername(jwt);
     Candidate candidate= candidateRepository.findCandidateByUsername(username);

     Boolean etat= this.applicationService.existsByInternshipIdAndCandidateId(internshipId,candidate.getId());
     return  ResponseEntity.ok(etat);

 }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
