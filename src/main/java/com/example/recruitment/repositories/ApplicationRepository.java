package com.example.recruitment.repositories;

import com.example.recruitment.models.Application;
import com.example.recruitment.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> findByCandidateId(Long candidateId);

}
