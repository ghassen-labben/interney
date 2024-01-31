package com.example.recruitment.services;

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

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    public Optional<Internship> getInternshipById(Long id) {
        return internshipRepository.findById(id);
    }

    public Internship saveInternship(Internship internship) {
        return internshipRepository.save(internship);
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
