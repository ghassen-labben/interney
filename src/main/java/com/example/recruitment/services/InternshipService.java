package com.example.recruitment.services;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final SkillService skillService;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository,SkillService skillService) {
        this.internshipRepository = internshipRepository;
        this.skillService=skillService;
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
    public PagedResponse<Internship> getInternshipsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Internship> internshipPage = internshipRepository.findAllByOrderByRegdateDesc(pageable);

        List<Internship> internships = new ArrayList<>(internshipPage.getContent());

        return new PagedResponse<>(
                internships,
                internshipPage.getNumber() + 1,
                internshipPage.getSize(),
                internshipPage.getTotalElements(),
                internshipPage.getTotalPages(),
                internshipPage.isLast()
        );
    }

    public Internship saveInternship(Internship internship) {
        List<Skill> skills=new ArrayList<>();
        for (Skill skill:internship.getSkills())
        {
            if(skillService.existByName(skill.getName()))
            {
                Optional<Skill> skillToAdd=skillService.getSkillByName(skill.getName());
                if(skillToAdd.isPresent())
                    skills.add(skillToAdd.get());
            }
            else {
                skill.setName(skill.getName().toUpperCase());
            skills.add(skill);
            }



        }
        for(Skill s:skills)
            System.out.println(s.getName());

        skillService.saveAll(skills);

        internship.setSkills(skills);
        return internshipRepository.save(internship);
    }
    public Internship addSkill(Internship internship, Skill skill) {
        skill.setName(skill.getName().toUpperCase());
        internship.addSkill(skill);
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
            internship.setDescription(updatedInternship.getDescription());

            return internshipRepository.save(internship);
        } else {
            return null;
        }
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }

    public List<Internship> getTop10() {

       List<Internship> internshipList =internshipRepository.findTopInternshipByNumberOfApplicants();
       if(internshipList.size()>10)
           return internshipList.subList(0,10);
        return internshipList;
    }
}
