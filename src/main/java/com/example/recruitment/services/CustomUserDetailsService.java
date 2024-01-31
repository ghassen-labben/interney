package com.example.recruitment.services;

import com.example.recruitment.models.Admin;
import com.example.recruitment.models.Candidate;
import com.example.recruitment.repositories.AdminRepository;
import com.example.recruitment.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CandidateRepository candidateRepository;
@Autowired
private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Candidate candidate = candidateRepository.findCandidateByUsername(username);
        if (candidate != null) {

            return org.springframework.security.core.userdetails.User
                    .withUsername(candidate.getUsername())
                    .password(candidate.getPassword())
                    .roles(candidate.getAuthorities().toString())
                    .build();
        }


        // Attempt to find admin
        Admin admin = adminRepository.findAdminByUsername(username);
        if (admin != null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.getUsername())
                    .password(admin.getPassword())
                    .roles(admin.getAuthorities().toString())
                    .build();
        }


        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
