package com.example.recruitment.repositories;

import com.example.recruitment.models.Admin;
import com.example.recruitment.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin,Long> {

     public Admin findAdminByUsername(String name);


}
