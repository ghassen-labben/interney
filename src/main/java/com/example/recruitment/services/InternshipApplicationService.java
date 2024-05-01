package com.example.recruitment.services;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.InternshipApplicationRepository;
import com.example.recruitment.repositories.TaskRepository;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.el.ELException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InternshipApplicationService {

    private final InternshipApplicationRepository internshipApplicationRepository;
    private final InternshipService internshipService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    @Autowired
    public InternshipApplicationService(TaskRepository taskRepository,InternshipApplicationRepository internshipApplicationRepository,UserRepository userRepository,InternshipService internshipService) {
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.internshipService=internshipService;
        this.userRepository=userRepository;
        this.taskRepository=taskRepository;
    }

    public void addTaskToInternshipApplication(InternshipApplication_Id internshipApplicationId, Task task) throws Exception {
        InternshipApplication internshipApplication = internshipApplicationRepository.findById(internshipApplicationId)
                .orElseThrow(() -> new Exception("Internship application not found"));
        if(!internshipApplication.isFullyAccepted())
            throw new Exception("should acceppt the application before");

        task.setInternshipApplication(internshipApplication);
        taskRepository.save(task);
    }

    public void markTaskAsStatus(Long taskId,Status status) throws Exception {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found"));

        task.setStatus(status);
        taskRepository.save(task);
    }
    public InternshipApplication saveInternshipApplication(Long internshipId, User user) throws Exception {
        InternshipApplication_Id internshipApplicationId = new InternshipApplication_Id(internshipId, user.getId());
        Optional<Internship> internship = internshipService.getInternshipById(internshipId);
        List<InternshipApplication> internshipApplications = this.getByCandidatId(user.getId());

        if(internshipApplications.size()==0) {
            InternshipApplication internshipApplication = new InternshipApplication(internshipApplicationId, user, internship.get());
            return internshipApplicationRepository.save(internshipApplication);
        }

        internshipApplications=internshipApplications.stream().filter(internshipApplication ->internshipApplication.isFullyAccepted() && internshipApplication.isFullyAccepted()).collect(Collectors.toList());
        if (internship.isPresent()) {
            boolean hasOverlappingInternship = false;
            if( overlaps(internshipApplications.get(0).getInternship().getStartDate(), internshipApplications.get(0).getInternship().getEndDate(), internship.get().getStartDate(), internship.get().getEndDate()))
                hasOverlappingInternship = true;

            if (hasOverlappingInternship) {
                throw new Exception("You have an overlapping internship during this period.");
            }

            InternshipApplication internshipApplication = new InternshipApplication(internshipApplicationId, user, internship.get());
            return internshipApplicationRepository.save(internshipApplication);
        } else {
            throw new Exception("Internship not found.");
        }
    }

    private boolean overlaps(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        return startDate1.before(endDate2) && endDate1.after(startDate2);
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
    public List<InternshipApplication> getByCandidatId(Long candidateId)
    {
        return this.internshipApplicationRepository.findAllByCandidateId(candidateId);
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


    public List<Task> getTasks(InternshipApplication_Id internshipApplicationId) throws Exception {
        Optional<InternshipApplication> internshipApplication=internshipApplicationRepository.findById(internshipApplicationId);
        if(internshipApplication.isPresent())
            return internshipApplication.get().getTasks();
        throw new Exception("no application are found");
    }
}
