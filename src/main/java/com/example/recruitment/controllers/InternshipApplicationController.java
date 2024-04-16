package com.example.recruitment.controllers;

import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.InternshipApplication_Id;
import com.example.recruitment.models.User;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.InternshipApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/internship-applications")
public class InternshipApplicationController {

    private final InternshipApplicationService internshipApplicationService;
    private final CustomUserDetailsService userDetailsService;
    @Autowired
    public InternshipApplicationController(InternshipApplicationService internshipApplicationService,CustomUserDetailsService userDetailsService) {
        this.internshipApplicationService = internshipApplicationService;
        this.userDetailsService=userDetailsService;
    }

    @PostMapping
    public ResponseEntity<?> saveInternshipApplication(HttpServletRequest request, @RequestParam Long internshipId) throws Exception {
        User user = userDetailsService.getUser(request);

        if (internshipApplicationService.hasUserAppliedForInternship(internshipId, user)) {
            return ResponseEntity.badRequest().body("You have already applied for this internship");
        }

        InternshipApplication savedApplication = internshipApplicationService.saveInternshipApplication(internshipId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }



    @GetMapping
    public ResponseEntity<List<InternshipApplication>> getAllInternshipApplications() {
        List<InternshipApplication> internshipApplications = internshipApplicationService.getAllInternshipApplications();
        return new ResponseEntity<>(internshipApplications, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Set<InternshipApplication>> getInternshipApplicationByUser(HttpServletRequest request) {
        User user = userDetailsService.getUser(request);

        Set<InternshipApplication> internshipApplications = user.getAppliedInternships();
        return new ResponseEntity<>(internshipApplications, HttpStatus.OK);
    }

    @GetMapping("/{candidateId}/{internshipId}")
    public ResponseEntity<InternshipApplication> getInternshipApplicationById(@PathVariable Long candidateId, @PathVariable Long internshipId) {
        InternshipApplication_Id id = new InternshipApplication_Id(candidateId, internshipId);
        System.out.println(id);
        Optional<InternshipApplication> internshipApplication = internshipApplicationService.getInternshipApplicationById(id);
        return internshipApplication.map(application -> new ResponseEntity<>(application, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{internshipId}")
    public ResponseEntity<Void> deleteInternshipApplication(HttpServletRequest request, @PathVariable Long internshipId) {;
        User user = userDetailsService.getUser(request);

        internshipApplicationService.deleteInternshipApplication(user.getId(), internshipId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
