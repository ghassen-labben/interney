package com.example.recruitment.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id")
    private Attachment cv;
    @CreationTimestamp
    private Timestamp ApplyDate;


}
