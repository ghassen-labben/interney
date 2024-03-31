package com.example.recruitment.services;

import com.example.recruitment.models.Attachment;
import com.example.recruitment.models.Profile;
import com.example.recruitment.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file, String attachmentType, Profile profile) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
            String baseDirectory = "";

            if (fileType != null && fileType.startsWith("image")) {
                baseDirectory = "images/";
            } else if (fileType != null && fileType.equals("application/pdf")) {
                baseDirectory = "documents/";
            }

            String filePath = baseDirectory + fileName;

            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFilePath(filePath);
            attachment.setFileType(file.getContentType());
            attachment.setProfile(profile); // Associate the attachment with the provided profile

            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }



    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));    }


}
