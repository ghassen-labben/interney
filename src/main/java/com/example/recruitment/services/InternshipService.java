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
    private final DepartmentService departmentService;

    @Autowired
    public InternshipService(InternshipRepository internshipRepository,SkillService skillService, DepartmentService departmentService) {
        this.internshipRepository = internshipRepository;
        this.skillService=skillService;
        this.departmentService=departmentService;
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
        try {

            Set<Skill> skills = new HashSet<>();
            for (Skill skill : internship.getSkills()) {
                if (skillService.existByName(skill.getName())) {
                    Optional<Skill> skillToAdd = skillService.getSkillByName(skill.getName());
                    if (skillToAdd.isPresent()) {
                        skills.add(skillToAdd.get());
                    } else {
                        System.out.println("Skill not found: " + skill.getName());
                    }
                } else {
                    skill.setName(skill.getName().toUpperCase());
                    skills.add(skill);
                }
            }

            Department department = departmentService.getDepartmentById(internship.getDepartment().getName());
            if (department == null) {
                System.out.println("Department not found: " + internship.getDepartment().getName());
            }

            department.addInternship(internship);
            departmentService.saveDepartment(department);
            List<Skill> skillsList = new ArrayList<>(skills);

            skillService.saveAll(skillsList);
            internship.setSkills(skills);

            Internship savedInternship = internshipRepository.save(internship);
            System.out.println("Saved internship: " + savedInternship);
            return savedInternship;
        } catch (Exception e) {
            System.out.println("Error saving internship: " + e.getMessage());
            return null;
        }
    }
    public Internship addSkill(Internship internship, Skill skill) {
        skill.setName(skill.getName().toUpperCase());
        internship.addSkill(skill);
        return internshipRepository.save(internship);
    }
    public Internship updateInternship(Long id, Internship updatedInternship) {
        Optional<Internship> internshipOptional = internshipRepository.findById(id);
System.out.println(updatedInternship.getDescription());
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


    public PagedResponse<Internship> getInternshipsByDeparmtentAndSearch(int page, int size, String departmentId,String searchQuery) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships;
        if(searchQuery!=null)
            internships   = internshipRepository.findByDepartmentNameAndTitleContainingIgnoreCase(departmentId,searchQuery,pageable);
        else
            internships = internshipRepository.findByDepartmentName(departmentId,pageable);

        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1,
                internships.getSize(), internships.getTotalElements(), internships.getTotalPages(),
                internships.isLast());
    }


    public PagedResponse<Internship> getInternshipsWithApplicant(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships;
        internships = internshipRepository.findAllWithApplicants(pageable);
        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1,
                internships.getSize(), internships.getTotalElements(), internships.getTotalPages(),
                internships.isLast());
    }

    public PagedResponse<Internship> getInternshipsBySkillsQueryAndDepartment(int page, int size, List<String> skills, String searchQuery, String departmentId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships;

        if (searchQuery != null) {
            internships = internshipRepository.findBySkillsNameInAndDepartmentNameAndTitleContainingIgnoreCase(skills, departmentId, searchQuery, pageable);
        } else {
            internships = internshipRepository.findBySkillsNameInAndDepartmentName(skills, departmentId, pageable);
        }

        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1, internships.getSize(), internships.getTotalElements(), internships.getTotalPages(), internships.isLast());
    }
    public PagedResponse<Internship> getInternshipsBySkillsAndDepartment(int page, int size, List<String> skills, String departmentId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findBySkillsNameInAndDepartmentName(skills, departmentId, pageable);

        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1, internships.getSize(), internships.getTotalElements(), internships.getTotalPages(), internships.isLast());
    }
    public PagedResponse<Internship> getInternshipsByQueryAndDepartment(int page, int size, String searchQuery, String departmentId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findByDepartmentNameAndTitleContainingIgnoreCase(departmentId, searchQuery, pageable);

        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1, internships.getSize(), internships.getTotalElements(), internships.getTotalPages(), internships.isLast());
    }
    public PagedResponse<Internship> getInternshipsByDepartment(int page, int size, String departmentId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "regdate");
        Page<Internship> internships = internshipRepository.findByDepartmentName(departmentId, pageable);

        return new PagedResponse<>(internships.getContent(), internships.getNumber() + 1, internships.getSize(), internships.getTotalElements(), internships.getTotalPages(), internships.isLast());
    }
}
