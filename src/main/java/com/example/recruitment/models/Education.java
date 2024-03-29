package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Education {
    private String school;
    private String degree;

    public Education(String school, String degree, String fieldOfStudy, LocalDate startDate, LocalDate endDate, String description) {
        this.school = school;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




}