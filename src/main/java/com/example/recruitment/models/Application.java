package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(ApplicationId.class)
public class Application implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @Id
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id")
    private Attachment cv;

    @CreationTimestamp
    private Timestamp applyDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus responseStatus = ApplicationStatus.PENDING;
}
