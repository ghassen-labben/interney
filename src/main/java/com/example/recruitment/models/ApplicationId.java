package com.example.recruitment.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;


public class ApplicationId implements Serializable {
    private Long internship;

    private Long candidate;
}
