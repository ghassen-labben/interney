package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.Education;
import com.example.recruitment.models.Profile;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.EducationService;
import com.example.recruitment.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/educations")
@CrossOrigin("http://localhost:4200")
public class EducationController {
    @Autowired
    private Utils jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EducationService educationService;

    @Autowired
    private ProfileService profileService;

    // Endpoint to get all educations
    @GetMapping
    public ResponseEntity<List<Education>> getAllEducations() {
        List<Education> educations = educationService.getAllEducations();
        return new ResponseEntity<>(educations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable("id") Long id) {
        Optional<Education> education = educationService.getEducationById(id);
        return education.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public ResponseEntity<Object> addEducation(@RequestBody Education education, HttpServletRequest request) {
        String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user=userRepository.findByUsername(username);
        Profile newprofile=new Profile();
        Optional<Profile> profile= Optional.ofNullable(user.getProfile());
        if (profile.isPresent()) {
            educationService.addEducation(education, profile.get());
            return new ResponseEntity<>(education, HttpStatus.CREATED);
        } else {
            profile= Optional.ofNullable(profileService.save(profile.get()));
            if(profile.isPresent())
                  newprofile=profile.get();
            educationService.getEducationsByProfile(education,newprofile);
            return new ResponseEntity<>(profile.get(),HttpStatus.CREATED);
        }
    }

    // Endpoint to update an education
    @PutMapping("/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable("id") Long id, @RequestBody Education educationDetails) {
        Education updatedEducation = educationService.updateEducation(id, educationDetails);
        if (updatedEducation != null) {
            return new ResponseEntity<>(updatedEducation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete an education
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable("id") Long id) {
        educationService.deleteEducation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
