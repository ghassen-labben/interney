package com.example.recruitment.services;

import com.example.recruitment.models.Education;
import com.example.recruitment.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {
    private final EducationRepository educationRepository;

    @Autowired
    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    // Create operation
    public Education saveOrUpdateEducation(Education education) {

            return educationRepository.save(education);

    }

    // Read operation - Get all educations
    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }

    // Read operation - Get education by ID
    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    // Update operation
    public Education updateEducation(Long id, Education educationDetails) {
        return educationRepository.findById(id)
                .map(education -> {
                    education.setSchool(educationDetails.getSchool());
                    education.setDegree(educationDetails.getDegree());
                    education.setFieldOfStudy(educationDetails.getFieldOfStudy());
                    education.setStartDate(educationDetails.getStartDate());
                    education.setEndDate(educationDetails.getEndDate());
                    education.setDescription(educationDetails.getDescription());
                    return educationRepository.save(education);
                })
                .orElse(null);
    }

    // Delete operation
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }
}
