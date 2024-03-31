package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.*;
import com.example.recruitment.repositories.EducationRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.AttachmentService;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin("http://localhost:4200")
public class ProfileController {
    @Autowired
    private Utils jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  AttachmentService attachmentService;
    private final ProfileService profileService;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        List<Profile> profiles = (List<Profile>) profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable("id") Long id) {
        Optional<Profile> profile = profileService.getProfileById(id);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/user")
    public ResponseEntity<Profile> getProfileByUser(HttpServletRequest request) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile = Optional.ofNullable(user.getProfile());
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(HttpServletRequest request,@RequestBody Profile profile) {
        String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user=userRepository.findByUsername(username);
        profile.setUser(user);
        Profile savedProfile = profileService.save(profile);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable("id") Long id, @RequestBody Profile profile) {
        if (!profileService.getProfileById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profile.setId(id);
        Profile updatedProfile = profileService.save(profile);
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
    @DeleteMapping("/education/{educationid}")
    public ResponseEntity<Void> removeEducation(HttpServletRequest request, @PathVariable("educationid") Long educationid) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();

        Iterator<Education> iterator = profile.getEducations().iterator();
        while (iterator.hasNext()) {
            Education education = iterator.next();
            if (education.getId().equals(educationid)) {
                educationRepository.delete(education);
                iterator.remove();
                break;
            }
        }

        profileService.save(profile);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/experiences")
    public ResponseEntity<Void> addExperience(HttpServletRequest request, @RequestBody Experience experience)
    {String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();
        profileService.addExperience(profile,experience);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/educations")
    public ResponseEntity<Void> addEducation(HttpServletRequest request, @RequestBody Education education)
    {String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();
        profileService.addEducation(profile,education);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/skills")
    public ResponseEntity<Void> addSkill(HttpServletRequest request, @RequestBody Skill skill) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();
        profileService.addSkill(profile, skill);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attachments")
    public ResponseEntity<Attachment> addAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();
        String fileType = file.getContentType();
        String attachmentType="";
        if (fileType != null && fileType.startsWith("image")) {
            attachmentType="image";
        }
        else {
            attachmentType="cv";
        }
        Attachment attachment = attachmentService.saveAttachment(file, attachmentType,profile);
        profileService.addAttachment(profile, attachment);
        return ResponseEntity.ok(attachment);
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> addContact(HttpServletRequest request, @RequestBody Contact contact) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Profile profile = user.getProfile();
        profileService.setContact(profile, contact);
        return ResponseEntity.noContent().build();
    }
}
