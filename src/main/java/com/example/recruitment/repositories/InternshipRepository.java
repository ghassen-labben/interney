package com.example.recruitment.repositories;

import com.example.recruitment.models.Department;
import com.example.recruitment.models.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship,Long> {
    public List<Internship> findInternshipsByDepartment(Department department);
}
