package com.example.recruitment.services;

import com.example.recruitment.models.Internship;
import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.InternshipApplication_Id;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.InternshipApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipService internshipService;

    @Autowired
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,InternshipService internshipService) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipService=internshipService;
    }

    public InternshipApplication saveInternshipApplication(Long internshipId, User user) throws Exception {
        InternshipApplication_Id internshipApplicationId=new InternshipApplication_Id(internshipId,user.getId());
        Optional<Internship> internship=internshipService.getInternshipById(internshipId);
        if(internship.isPresent())
        {
            InternshipApplication internshipApplication=new InternshipApplication(internshipApplicationId,user,internship.get());
            return internshipApplicationRepository.save(internshipApplication);

        }
        else
            throw new Exception("internship not found");
    }

    public List<InternshipApplication> getAllInternshipApplications() {
        return internshipApplicationRepository.findAll();
    }

    public Optional<InternshipApplication> getInternshipApplicationById(InternshipApplication_Id id) {
        return internshipApplicationRepository.findById(id);
    }

    @Transactional
    public void deleteInternshipApplication(Long candiateid,Long internshipId) {
        internshipApplicationRepository.deleteByCandidateIdAndInternshipId(candiateid,internshipId);
    }

    public boolean hasUserAppliedForInternship(Long internshipId, User user) {
        return internshipApplicationRepository.existsByInternshipIdAndCandidate(internshipId, user);
    }
}
