package com.example.recruitment.services;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final InternshipService internshipService;
    private final CandidateService candidateService;
    private final AttachmentService attachmentService;
    private final ApplicationRepository applicationRepository;
    @Transactional
    public void updateApplicationStatus(Long internshipId, Long candidateId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findByInternshipIdAndCandidateId(internshipId, candidateId);
        application.setResponseStatus(newStatus);
        applicationRepository.save(application);
    }
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
    public Application saveApplicationWithDetails(Internship internship, Candidate candidate, String attachmentId) throws Exception {
        if (internship == null) {
            throw new Exception("Internship cannot be null");
        }

        if (candidate == null) {
            throw new Exception("Candidate cannot be null");
        }

        if (applicationRepository.existsByInternshipIdAndCandidateId(internship.getId(), candidate.getId())) {
            throw new Exception("You have already applied for this internship");
        }

        Attachment attachment = attachmentService.getAttachment(attachmentId);
        Application application = new Application();
        application.setInternship(internship);
        application.setCandidate(candidate);
        application.setCv(attachment);
        return applicationRepository.save(application);
    }

    public boolean existsByInternshipIdAndCandidateId(Long internshipId, Long candidateId)
    {
        return  this.applicationRepository.existsByInternshipIdAndCandidateId(internshipId,candidateId);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
