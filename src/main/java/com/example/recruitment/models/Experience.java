package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Experience {
    private String company;
    private String jobTitle;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Experience(String company, String jobTitle, String description, LocalDate startDate, LocalDate endDate) {
        this.company = company;
        this.jobTitle = jobTitle;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}