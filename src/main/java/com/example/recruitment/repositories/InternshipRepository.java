package com.example.recruitment.repositories;

import com.example.recruitment.models.Department;
import com.example.recruitment.models.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InternshipRepository extends JpaRepository<Internship,Long> {
    public List<Internship> findInternshipsByDepartment(Department department);
}
