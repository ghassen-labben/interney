package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "DEADLINE")
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;
    public Task(String title, String description, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }



    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id"),
            @JoinColumn(name = "internship_id", referencedColumnName = "internship_id")
    })
    private InternshipApplication internshipApplication;
}
