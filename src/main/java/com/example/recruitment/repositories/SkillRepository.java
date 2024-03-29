package com.example.recruitment.repositories;

import com.example.recruitment.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,String> {
}
