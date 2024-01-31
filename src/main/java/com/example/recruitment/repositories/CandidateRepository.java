package com.example.recruitment.repositories;

import com.example.recruitment.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    public Candidate findCandidateByUsername(String name);
}
