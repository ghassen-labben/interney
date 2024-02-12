package com.example.recruitment.services;

import com.example.recruitment.models.Department;
import com.example.recruitment.models.Internship;
import com.example.recruitment.repositories.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    public List<Internship> getInternshipsByDepartment(Department department) {
        return internshipRepository.findInternshipsByDepartment(department);
    }

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    public Optional<Internship> getInternshipById(Long id) {
        return internshipRepository.findById(id);
    }

    public Internship saveInternship(Internship internship) {
        return internshipRepository.save(internship);
    }

    public Internship updateInternship(Long id, Internship updatedInternship) {
        Optional<Internship> internshipOptional = internshipRepository.findById(id);

        if (internshipOptional.isPresent()) {
            Internship internship = internshipOptional.get();
            internship.setDepartment(updatedInternship.getDepartment());
            internship.setSkills(updatedInternship.getSkills());
            internship.setEndDate(updatedInternship.getEndDate());
            internship.setStartDate(updatedInternship.getStartDate());
            internship.setTitle(updatedInternship.getTitle());

            return internshipRepository.save(internship);
        } else {
            return null;
        }
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
