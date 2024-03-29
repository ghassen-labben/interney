package com.example.recruitment.services;

import com.example.recruitment.models.Authority;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

@Autowired
private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user != null) {

            List<SimpleGrantedAuthority> grantedAuthorities = user
                    .getAuthorities()
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                    .toList();

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);



        }


        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
