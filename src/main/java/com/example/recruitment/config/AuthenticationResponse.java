package com.example.recruitment.config;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class AuthenticationResponse implements Serializable {
    private final String jwt;
    private final String auth;

    public AuthenticationResponse(String jwt, String auth) {
        this.jwt = jwt;
        this.auth = auth;
    }

}
