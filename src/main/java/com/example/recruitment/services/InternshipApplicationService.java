package com.example.recruitment.services;

import com.example.recruitment.models.Internship;
import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.InternshipApplication_Id;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.InternshipApplicationRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipService internshipService;
    private final UserRepository userRepository;

    @Autowired
    public InternshipApplicationService(InternshipApplicationRepository internshipApplicationRepository,UserRepository userRepository,InternshipService internshipService) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipService=internshipService;
        this.userRepository=userRepository;
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


    public InternshipApplication assignEncadrantToInternshipApplication(Long candidateId,Long InternshipId, Long encadrantId) throws Exception {
        InternshipApplication_Id applicationId=new InternshipApplication_Id(InternshipId,candidateId);
        InternshipApplication internshipApplication = internshipApplicationRepository.findById(applicationId).orElse(null);
        if (internshipApplication != null) {
            Optional<User> userOptional=userRepository.findById(encadrantId);
            User user=userOptional.get();
            if(userOptional.isPresent()) {
                user.addApplicationSupervisees(internshipApplication);
                userRepository.save(user);
                internshipApplication.setEncadrant(user);
            }
          return  internshipApplicationRepository.save(internshipApplication);
        }
        return null;
    }
    public List<InternshipApplication> getByEncadrantId(Long encadrantId)
    {
        return this.internshipApplicationRepository.findAllByEncadrantId(encadrantId);
    }

    public List<InternshipApplication> getInternshipApplicationByInternshipId(Long internshipId) {
        return this.internshipApplicationRepository.findAllByInternshipId(internshipId);

    }

    public void traiterApplication(User user, Long candidateId, Long internshipId, Boolean status) {
        InternshipApplication_Id applicationId = new InternshipApplication_Id(internshipId, candidateId);
        InternshipApplication internshipApplication = internshipApplicationRepository.findById(applicationId).orElse(null);
        if (internshipApplication != null) {
            if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_RH"))) {
                internshipApplication.setHrAccepted(status);
            } else if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ENCADRANT"))
                    && internshipApplication.getHrAccepted() != null && internshipApplication.getHrAccepted()) {

                internshipApplication.setEncadrantAccepted(status);
            }
            this.internshipApplicationRepository.save(internshipApplication);
        }
    }



}
