package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.InternshipApplication_Id;
import com.example.recruitment.models.User;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.InternshipApplicationService;
import com.example.recruitment.services.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Utils jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    @Autowired
    public InternshipApplicationController(InternshipApplicationService internshipApplicationService, Utils jwtUtil, CustomUserDetailsService userDetailsService) {
        this.internshipApplicationService = internshipApplicationService;
        this.jwtUtil = jwtUtil;
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
    @GetMapping("/internship/{internshipId}")
    public List<InternshipApplication> getInternshipApplicationByInternshipId(@PathVariable Long internshipId) {
         List<InternshipApplication> internshipApplications = internshipApplicationService.getInternshipApplicationByInternshipId(internshipId);
        return internshipApplications;
    }
    @GetMapping("/encadrant/{encadrantId}")
    public List<InternshipApplication> getInternshipApplicationByEncadrantId(@PathVariable Long encadrantId) {
        List<InternshipApplication> internshipApplications = internshipApplicationService.getByEncadrantId(encadrantId);
        return internshipApplications;
    }
    @DeleteMapping("/{internshipId}")
    public ResponseEntity<Void> deleteInternshipApplication(HttpServletRequest request, @PathVariable Long internshipId) {;
        User user = userDetailsService.getUser(request);

        internshipApplicationService.deleteInternshipApplication(user.getId(), internshipId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{internshipId}/{candidateId}/assign-encadrant/{encadrantId}")
    public ResponseEntity<InternshipApplication> assignEncadrantToInternshipApplication(@PathVariable Long candidateId,@PathVariable Long internshipId, @PathVariable Long encadrantId) throws Exception {
       InternshipApplication internshipApplication= internshipApplicationService.assignEncadrantToInternshipApplication(candidateId,internshipId, encadrantId);
        return ResponseEntity.ok(internshipApplication);
    }
    @PutMapping("/{internshipId}/{candidateId}/status/{status}")
    public ResponseEntity<Void> applicationTraitment(HttpServletRequest request, @PathVariable Long candidateId,@PathVariable Long internshipId, @PathVariable Boolean status) {
        User user = userDetailsService.getUser(request);
        internshipApplicationService.traiterApplication(user,candidateId,internshipId, status);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{encadrantId}")
    public ResponseEntity<List<InternshipApplication>> getInternshipApplicationById(@PathVariable Long encadrantId) {
        List<InternshipApplication> internshipApplications = internshipApplicationService.getByEncadrantId(encadrantId);
        return ResponseEntity.ok(internshipApplications);
    }

    @GetMapping("/encadrant")
    public ResponseEntity<List<InternshipApplication>> getInternshipApplicationById(HttpServletRequest request) {
        User user=userDetailsService.getUser(request);
        List<InternshipApplication> internshipApplications = internshipApplicationService.getByEncadrantId(user.getId());
        return ResponseEntity.ok(internshipApplications);
    }
}
