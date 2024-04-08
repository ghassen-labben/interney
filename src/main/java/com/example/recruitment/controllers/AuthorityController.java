package com.example.recruitment.controllers;

import com.example.recruitment.models.Authority;
import com.example.recruitment.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
@CrossOrigin("http://localhost:4200")
public class AuthorityController {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityController(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @GetMapping
    public ResponseEntity<List<Authority>> getAuthorities() {
        try {
            List<Authority> authorities = authorityRepository.findAll();
            return ResponseEntity.ok(authorities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
