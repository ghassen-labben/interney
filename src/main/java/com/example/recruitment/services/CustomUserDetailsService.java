package com.example.recruitment.services;

import com.example.recruitment.config.Utils;
import com.example.recruitment.models.Authority;
import com.example.recruitment.models.InternshipApplication;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

@Autowired
private UserRepository userRepository;
    @Autowired
    private Utils jwtTokenUtil;
    public void saveUser(User user) {
        User user1=userRepository.findByUsername(user.getUsername());
        user1.setStatus(Boolean.TRUE);
        userRepository.save(user1);
    }

    public void disconnect(User user) {
        var storedUser = userRepository.findByUsername(user.getUsername());
        if (storedUser != null) {
            storedUser.setStatus(Boolean.FALSE);
            userRepository.save(storedUser);
        }
    }

    public User getUserByUsername(String username)
    {
        User user=new User();
        if(isEmail(username))
            user=userRepository.findUserByEmail(username);
        else
            user = userRepository.findByUsername(username);
        return user;
    }
    public Set<User> findConnectedUsers(HttpServletRequest request){
        User user=this.getUser(request);
        if(user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ENCADRANT") ))
        {
            Set<User> candidates = user.getApplicationsSupervisees().stream()
                    .filter(application -> application.getEncadrantAccepted())
                    .map(InternshipApplication::getCandidate)
                    .collect(Collectors.toSet());
            return  candidates;
        }
        return userRepository.findEncadrantByCandidate(user);
    }
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

    public User getUser(HttpServletRequest request)
    {
        String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
        User user=userRepository.findByUsername(username);
        return user;
    }
    public boolean isEmail(String inputString) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(emailPattern);

        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    public List<User> findByAuthority(String roleUser) {
        return userRepository.findByAuthorityName(roleUser);
    }
}
