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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomUserDetailsService implements UserDetailsService {

@Autowired
private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=null;
        if(isEmail(username))
            user=userRepository.findUserByEmail(username);
        else
            user = userRepository.findByUsername(username);



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
    public boolean isEmail(String inputString) {
        // Regular expression pattern for matching an email address
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // Use Pattern class to compile the regex pattern
        Pattern pattern = Pattern.compile(emailPattern);

        // Use Matcher class to match the input string with the pattern
        Matcher matcher = pattern.matcher(inputString);

        // Return true if the input string matches the pattern
        return matcher.matches();
    }
}
