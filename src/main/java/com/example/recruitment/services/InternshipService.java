package com.example.recruitment.services;

import com.example.recruitment.models.*;
import com.example.recruitment.repositories.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final SkillService skillService;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository,SkillService skillService) {
        this.internshipRepository = internshipRepository;
        this.skillService=skillService;
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
        Set<Skill> skills=new HashSet<>();
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

        skillService.saveAll((List<Skill>) skills);

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
    public PagedResponse<Internship> getInternshipsBySkills(int page, int size, List<String> skills) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findBySkillsNameIn(skills, pageable);
        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1,
                internships.getSize(), internships.getTotalElements(), internships.getTotalPages(),
                internships.isLast());
    }

    public PagedResponse<Internship> getInternshipsByQuery(int page, int size, String searchQuery) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findByTitleContainingIgnoreCase(searchQuery, pageable);
        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1,
                internships.getSize(), internships.getTotalElements(), internships.getTotalPages(),
                internships.isLast());
    }

    public PagedResponse<Internship> getInternshipsBySkillsAndQuery(int page, int size, List<String> skills, String searchQuery) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findBySkillsNameInAndTitleContainingIgnoreCase(skills, searchQuery, pageable);
        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1,
                internships.getSize(), internships.getTotalElements(), internships.getTotalPages(),
                internships.isLast());
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
