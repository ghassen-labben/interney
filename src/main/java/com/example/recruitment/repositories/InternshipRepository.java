package com.example.recruitment.repositories;

import com.example.recruitment.models.Internship;
import com.example.recruitment.models.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface InternshipRepository extends JpaRepository<Internship,Long> {
    Page<Internship> findAllByOrderByRegdateDesc(Pageable pageable);

    @Query("SELECT i FROM Internship i JOIN i.internshipApplications ia GROUP BY i.id ORDER BY COUNT(ia) DESC ")
    List<Internship> findTopInternshipByNumberOfApplicants();

    Page<Internship> findBySkillsNameIn(List<String> skills, Pageable pageable);

    Page<Internship> findByTitleContainingIgnoreCase(String searchQuery, Pageable pageable);

    Page<Internship> findBySkillsNameInAndTitleContainingIgnoreCase(List<String> skills, String searchQuery, Pageable pageable);
}
