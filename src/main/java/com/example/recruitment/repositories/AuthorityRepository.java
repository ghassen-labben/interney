package com.example.recruitment.repositories;

import com.example.recruitment.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    boolean existsByAuthority(String authority);
}

