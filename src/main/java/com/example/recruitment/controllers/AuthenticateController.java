package com.example.recruitment.controllers;

import com.example.recruitment.config.AuthenticationRequest;
import com.example.recruitment.config.AuthenticationResponse;
import com.example.recruitment.config.Utils;
import com.example.recruitment.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("http://localhost:4200")
public class AuthenticateController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Utils jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;



    @GetMapping( "/user" )
    public UserDetails firstPage(HttpServletRequest request) {
String username=jwtTokenUtil.extractUsername(request.getHeader("Authorization").split(" ")[1]);
UserDetails userDetails=userDetailsService.loadUserByUsername(username);

        return userDetails;
    }



    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
      String auth=  userDetails.getAuthorities().toString().substring(7,userDetails.getAuthorities().toString().length()-2);
        return new AuthenticationResponse(jwt,auth);
    }
}
