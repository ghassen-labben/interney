package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.Profile;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    private Utils jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        List<Profile> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable("id") Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
//salah fazet skils mayettzadoch
    @PostMapping
    public ResponseEntity<Profile> createProfile(HttpServletRequest request,@RequestBody Profile profile) {
        String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user=userRepository.findByUsername(username);
profile.setUser(user);
        Profile savedProfile = profileService.saveOrUpdateProfile(profile);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable("id") Long id, @RequestBody Profile profile) {
        if (!profileService.getProfileById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profile.setId(id);
        Profile updatedProfile = profileService.saveOrUpdateProfile(profile);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id) {
        if (!profileService.getProfileById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profileService.deleteProfileById(id);
        return ResponseEntity.noContent().build();
    }
}
