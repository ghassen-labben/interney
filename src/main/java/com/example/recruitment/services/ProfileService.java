package com.example.recruitment.services;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.AttachmentRepository;
import com.example.recruitment.repositories.ExperienceRepository;
import com.example.recruitment.repositories.ProfileRepository;
import com.example.recruitment.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;

    private final SkillRepository skillRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, ExperienceRepository experienceRepository, SkillRepository skillRepository) {
        this.profileRepository = profileRepository;
        this.experienceRepository = experienceRepository;
        this.skillRepository = skillRepository;
    }

    public Profile save(Profile profile) {
        if (profile == null) {
            profile = new Profile();
        }
        return profileRepository.save(profile);
    }

    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile addExperience(Profile profile, Experience experience) {
        experience.setProfile(profile);
        profile.addExperience(experience);
        return profileRepository.save(profile);
    }

    public Profile addEducation(Profile profile, Education education) {
        education.setProfile(profile);

        profile.addEducation(education);
        return profileRepository.save(profile);
    }

    public Profile addSkill(Profile profile, Skill skill) {
        skill.setName(skill.getName().toUpperCase());
        profile.addSkill(skill);
        return profileRepository.save(profile);
    }

    public Profile removeSkill(Profile profile, Skill skill) {
        profile.removeSkill(skill);
        return profileRepository.save(profile);
    }

    public Profile addAttachment(Profile profile, Attachment attachment) {
        attachment=attachmentRepository.save(attachment);
        if (attachment.getFileType() != null && attachment.getFileType().startsWith("image")) {
            profile.setProfileImage(attachment);
        }
        else
            profile.setCv(attachment);
        return profileRepository.save(profile);
    }



    public Profile setContact(Profile profile, Contact contact) {
        Contact oldContact=profile.getContact();
        if(oldContact==null)
        {
            profile.setContact(contact);

        }
        else if(oldContact!=null)
        {
            if(oldContact.equals(contact))
                return profileRepository.save(profile);
            if(contact.getPays()!=null && !contact.getPays().equals(oldContact.getPays()))
            {
                oldContact.setPays(contact.getPays());
            }
            if(contact.getRue()!=null && !contact.getRue().equals(oldContact.getRue()) )
            {
                oldContact.setRue(contact.getRue());
            }
            if (contact.getAddressComplete()!=null && !contact.getAddressComplete().equals(oldContact.getAddressComplete()))
            {
                oldContact.setAddressComplete(contact.getAddressComplete());
            }
            if(contact.getCodePostal()!=null && !contact.getCodePostal().equals(oldContact.getCodePostal()))
            {
                oldContact.setCodePostal(contact.getCodePostal());
            }
            if (contact.getVille()!=null && !contact.getVille().equals(oldContact.getVille()))
            {
                oldContact.setVille(contact.getVille());
            }
            if( contact.getLinkedin()!=null && !contact.getLinkedin().equals(oldContact.getLinkedin()))
            {
                oldContact.setLinkedin(contact.getLinkedin());
            }
            if (contact.getWhatsapp()!=null && !contact.getWhatsapp().equals(oldContact.getWhatsapp()))
            {
                oldContact.setWhatsapp(contact.getWhatsapp());
            }
            if(contact.getTelephone()!=null && !contact.getTelephone().equals(oldContact.getTelephone()))
            {
                oldContact.setTelephone(contact.getTelephone());

            }
            profile.setContact(oldContact);

        }
        return profileRepository.save(profile);

    }

    public void deleteProfileById(Long id) {
        profileRepository.deleteById(id);
    }


}
