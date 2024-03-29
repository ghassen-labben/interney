package com.example.recruitment.services;

import com.example.recruitment.models.Profile;
import com.example.recruitment.models.Skill;
import com.example.recruitment.repositories.ProfileRepository;
import com.example.recruitment.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final SkillRepository skillRepository; // Assuming you have a SkillRepository

    @Autowired
    public ProfileService(ProfileRepository profileRepository, SkillRepository skillRepository) {
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
    }

    // Other methods...

    public Profile saveOrUpdateProfile(Profile profile) {
        if (profile.getId() != null) {
            // Profile already exists in the database, update it
            Optional<Profile> existingProfileOptional = profileRepository.findById(profile.getId());
            if (existingProfileOptional.isPresent()) {
                Profile existingProfile = existingProfileOptional.get();
                // Update existing profile fields
                existingProfile.setUser(profile.getUser());
                existingProfile.setExperiences(profile.getExperiences());
                existingProfile.setEducations(profile.getEducations());
                existingProfile.setContact(profile.getContact());
                existingProfile.setAttachments(profile.getAttachments());
                // Update existing skills (check and associate existing ones)
                List<Skill> existingSkills = new ArrayList<>();
                for (Skill skill : profile.getSkills()) {
                    Optional<Skill> existingSkillOptional = skillRepository.findById(skill.getName());
                    if (existingSkillOptional.isPresent()) {
                        existingSkills.add(existingSkillOptional.get());
                    } else {
                        existingSkills.add(skill);
                    }
                }
                existingProfile.setSkills(existingSkills);
                return profileRepository.save(existingProfile); // Update the existing profile
            } else {
                // Profile not found in the database, save it as a new entry
                return profileRepository.save(profile);
            }
        } else {
            // Profile doesn't have an ID, save it as a new entry
            return profileRepository.save(profile);
        }
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }



    public void deleteProfileById(Long id) {
        profileRepository.deleteById(id);
    }
}
