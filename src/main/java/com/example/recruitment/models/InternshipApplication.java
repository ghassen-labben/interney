package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InternshipApplication implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @EmbeddedId
    InternshipApplication_Id id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    User candidate;


    @ManyToOne()
    @MapsId("internshipId")
    @JoinColumn(name = "internship_id")
    Internship internship;

    public InternshipApplication(InternshipApplication_Id id, User candidate, Internship internship) {
        this.id = id;
        this.candidate = candidate;
        this.internship = internship;
    }

    @CreationTimestamp
    private Timestamp applyDate;

    @Column(name = "isAccepted",nullable = true)
    private Boolean isAccepted=null;
}
