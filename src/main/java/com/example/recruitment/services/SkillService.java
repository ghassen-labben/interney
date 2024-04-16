package com.example.recruitment.services;

import com.example.recruitment.models.Skill;
import com.example.recruitment.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> getSkillByName(String name) {
        return skillRepository.findById(name);
    }
    public Boolean existByName(String name)
    {
        return skillRepository.existsById(name);
    }
    public void deleteSkill(String name)
    {
         skillRepository.deleteById(name);
    }
    public void saveAll(List<Skill> skillList)
    {
        skillRepository.saveAll(skillList);
    }

}
