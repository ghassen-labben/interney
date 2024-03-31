package com.example.recruitment.services;

import com.example.recruitment.models.Education;
import com.example.recruitment.models.Profile;
import com.example.recruitment.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {
    private final EducationRepository educationRepository;
    @Autowired
    private ProfileService profileService;

    @Autowired
    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }
    public Education save(Education education) {
            return educationRepository.save(education);
    }
    public List<Education> getEducationsByProfile(Education education, Profile profile)
    {
        return this.educationRepository.findEducationsByProfile(profile);
    }
    public void addEducation(Education education,Profile profile)
    {
        education =educationRepository.save(education);
        profile.addEducation(education);
        profileService.save(profile);

    }

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
