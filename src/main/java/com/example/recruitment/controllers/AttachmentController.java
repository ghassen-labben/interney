package com.example.recruitment.controllers;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.Attachment;
import com.example.recruitment.models.Profile;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.AttachmentRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/attachment")
@CrossOrigin("http://localhost:4200")

public class AttachmentController {

    @Autowired
    private final AttachmentService attachmentService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private Utils jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("attachmentType") String attachmentType, HttpServletRequest request) {
        try {
            String username = jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
            User user = userRepository.findByUsername(username);

            // Check if the user is authenticated and authorized to upload files
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Find or create the profile for the current user
            Profile profile = user.getProfile();
            if (profile == null) {
                // Handle case where profile doesn't exist
                // You may create a new profile for the user if needed
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // Save the attachment
            Attachment attachment = attachmentService.saveAttachment(file, attachmentType);

            // Associate the attachment with the profile
            attachment.setProfile(profile);
            attachment = attachmentRepository.save(attachment);

            // Generate the download URL
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/attachment/download/")
                    .path(attachment.getId())
                    .toUriString();

            // Return the saved attachment with OK status
            return ResponseEntity.ok(attachment);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String id) {
        try {
            Attachment attachment = attachmentService.getAttachment(id);

            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(attachment.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                        .body(resource);
            } else {
                // Handle the case where the file is not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exception (e.g., log error, return appropriate response)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}