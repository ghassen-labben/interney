package com.example.recruitment.services;

import com.example.recruitment.models.Admin;
import com.example.recruitment.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin findByName(String name)
    {
        return this.adminRepository.findAdminByUsername(name);
    }
}
