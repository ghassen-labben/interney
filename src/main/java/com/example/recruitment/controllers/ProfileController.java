package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.*;
import com.example.recruitment.repositories.EducationRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.AttachmentService;
import com.example.recruitment.services.CustomUserDetailsService;
import com.example.recruitment.services.ProfileService;
import com.example.recruitment.services.SkillService;
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
import java.util.Set;

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
    private SkillService skillService;
    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @GetMapping("/user")
    public ResponseEntity<Profile> getProfileByUser(HttpServletRequest request) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile=profileService.getProfileByUser(user);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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


    @GetMapping("/user/{id}")
    public ResponseEntity<Profile> getProfileByUserId(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Optional<Profile> profile = profileService.getProfileByUser(user.get());
            return profile.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
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
        Optional<Profile> profile = profileService.getProfileByUser(user);

        Iterator<Education> iterator = profile.get().getEducations().iterator();
        while (iterator.hasNext()) {
            Education education = iterator.next();
            if (education.getId().equals(educationid)) {
                educationRepository.delete(education);
                iterator.remove();
                break;
            }
        }

        profileService.save(profile.get());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/skill/{skillId}")
    public ResponseEntity<Void> removeSkill(HttpServletRequest request, @PathVariable("skillId") String skillId) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile = profileService.getProfileByUser(user);
        profile.get().removeSkill(skillId);
        profileService.save(profile.get());
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/experiences")
    public ResponseEntity<Void> addExperience(HttpServletRequest request, @RequestBody Experience experience)
    {String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile = profileService.getProfileByUser(user);
        profileService.addExperience(profile.get(),experience);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/educations")
    public ResponseEntity<Void> addEducation(HttpServletRequest request, @RequestBody Education education)
    {String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile =profileService.getProfileByUser(user);
        profileService.addEducation(profile.get(),education);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/skills")
    public ResponseEntity<Void> addSkill(HttpServletRequest request, @RequestBody Skill skill) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile = profileService.getProfileByUser(user);
        profileService.addSkill(profile.get(), skill);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attachments")
    public ResponseEntity<Attachment> addAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile = profileService.getProfileByUser(user);
        String fileType = file.getContentType();
        String attachmentType="";
        if (fileType != null && fileType.startsWith("image")) {
            attachmentType="image";
        }
        else {
            attachmentType="cv";
        }
        Attachment attachment = attachmentService.saveAttachment(file, attachmentType,profile.get());
        profileService.addAttachment(profile.get(), attachment);
        return ResponseEntity.ok(attachment);
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> addContact(HttpServletRequest request, @RequestBody Contact contact) {
        String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByUsername(username);
        Optional<Profile> profile =profileService.getProfileByUser(user);
        profileService.setContact(profile.get(), contact);
        return ResponseEntity.noContent().build();
    }
}
