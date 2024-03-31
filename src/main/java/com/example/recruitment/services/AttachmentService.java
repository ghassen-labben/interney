package com.example.recruitment.services;

import com.example.recruitment.models.Attachment;
import com.example.recruitment.models.Profile;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file, String attachmentType, Profile profile) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

}
