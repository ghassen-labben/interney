package com.example.recruitment.repositories;

import com.example.recruitment.models.Experience;
import com.example.recruitment.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience,Long> {
}
