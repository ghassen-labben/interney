package com.example.recruitment.config;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
    private final String jwt;
    private final String authorities;

    public AuthenticationResponse(String jwt, String authorities) {
        this.jwt = jwt;
        this.authorities = authorities;
    }

    public String getAuthorities() {
        return authorities;
    }

    public String getJwt() {
        return jwt;
    }
}
