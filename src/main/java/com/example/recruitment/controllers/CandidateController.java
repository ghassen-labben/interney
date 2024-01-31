package com.example.recruitment.controllers;

import com.example.recruitment.models.Candidate;
import com.example.recruitment.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// CandidateController.java
@RestController
@RequestMapping("/candidates")
@CrossOrigin("http://localhost:4200")

public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Optional<Candidate> candidate = candidateService.getCandidateById(id);
        return candidate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Candidate> saveCandidate(@RequestBody Candidate candidate) {
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
String password=bCryptPasswordEncoder.encode(candidate.getPassword());
                candidate.setPassword(password);

            Candidate savedCandidate = candidateService.saveCandidate(candidate);
            return new ResponseEntity<>(savedCandidate, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /*
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate candidate) {
        try {
            Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
            return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
