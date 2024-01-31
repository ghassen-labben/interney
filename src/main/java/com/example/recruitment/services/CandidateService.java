package com.example.recruitment.services;

import com.example.recruitment.models.Candidate;
import com.example.recruitment.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
    public Candidate loadUserByUsername(String username)
    {
        return candidateRepository.findCandidateByUsername(username);
    }

    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    public Candidate saveCandidate(Candidate candidate) {

        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
