package com.example.recruitment.controllers;

import com.example.recruitment.models.Attachment;
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

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/attachment")
@CrossOrigin("http://localhost:4200")

public class AttachmentController {

    @Autowired
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Attachment attachment = attachmentService.saveAttachment(file);

            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/attachment/download/")
                    .path(attachment.getId())
                    .toUriString();

           return  ResponseEntity.ok(attachment);

        } catch (Exception e) {
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