package com.example.recruitment.repositories;

import com.example.recruitment.models.Education;
import com.example.recruitment.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education,Long> {
}
