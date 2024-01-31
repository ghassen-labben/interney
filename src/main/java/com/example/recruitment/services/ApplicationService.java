package com.example.recruitment.services;

import com.example.recruitment.models.Application;
import com.example.recruitment.models.Attachment;
import com.example.recruitment.models.Candidate;
import com.example.recruitment.models.Internship;
import com.example.recruitment.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final InternshipService internshipService;
    private final CandidateService candidateService;
    private final AttachmentService attachmentService;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,
                              InternshipService internshipService,
                              CandidateService candidateService,
                              AttachmentService attachmentService) {
        this.applicationRepository = applicationRepository;
        this.internshipService = internshipService;
        this.candidateService = candidateService;
        this.attachmentService = attachmentService;
    }


    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }
public List<Application> getApplicationsByCandidate(Long candidateid){return applicationRepository.findByCandidateId(candidateid);}
    public Application saveApplicationWithDetails(
                                                  Long internshipId,
                                                  Long candidateId,
                                                  String attachmentId) throws Exception {
        Internship internship = internshipService.getInternshipById(internshipId)
                .orElseThrow(() -> new Exception("Internship not found"));

        Candidate candidate = candidateService.getCandidateById(candidateId)
                .orElseThrow(() -> new Exception("Candidate not found"));

        Attachment attachment = attachmentService.getAttachment(attachmentId);
Application application=new Application();
        application.setInternship(internship);
        application.setCandidate(candidate);
        application.setCv(attachment);


        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
