package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Experience implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}