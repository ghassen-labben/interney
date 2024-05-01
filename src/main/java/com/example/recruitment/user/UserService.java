package com.example.recruitment.user;


import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import com.example.recruitment.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

}
