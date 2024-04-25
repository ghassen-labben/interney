package com.example.recruitment.repositories;

import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.InternshipApplication_Id;
import com.example.recruitment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, InternshipApplication_Id> {

    boolean existsByInternshipIdAndCandidate(Long internshipId, User user);
    @Modifying
    @Query("delete from InternshipApplication ia where ia.id.candidateId = :candidateId and ia.id.internshipId = :internshipId")
    void deleteByCandidateIdAndInternshipId(@Param("candidateId") Long candidateId, @Param("internshipId") Long internshipId);


    List<InternshipApplication> findAllByEncadrantId(Long encadrantId);
    @Query("select  ia from InternshipApplication ia where ia.id.internshipId = :internshipId")
    List<InternshipApplication> findAllByInternshipId(Long internshipId);
}
