package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InternshipApplication implements Serializable {
    @Serial
    private static final long serialVersionUID = 9178661439383356177L;

    @EmbeddedId
    InternshipApplication_Id id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    User candidate;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @MapsId("internshipId")
    @JoinColumn(name = "internship_id")
    Internship internship;

    @ManyToOne
    @JoinColumn(name = "encadrant_id")
    private User encadrant;


    public InternshipApplication(InternshipApplication_Id id, User candidate, Internship internship) {
        this.id = id;
        this.candidate = candidate;
        this.internship = internship;
    }


    @JsonIgnore
    @OneToMany(mappedBy = "internshipApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();


    public void addTask(Task task) {
        tasks.add(task);
        task.setInternshipApplication(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setInternshipApplication(null);
    }

    @CreationTimestamp
    private Timestamp applyDate;


    @Column(name = "hrAccepted", nullable = true)
    private Boolean hrAccepted = null;

    @Column(name = "encadrantAccepted", nullable = true)
    private Boolean encadrantAccepted = null;

    public void hrAccept() {
        this.hrAccepted = true;
    }

    public void encadrantAccept() {
        this.encadrantAccepted = true;
    }

    public boolean isFullyAccepted() {
        return hrAccepted != null && encadrantAccepted != null && hrAccepted && encadrantAccepted;
    }

}
