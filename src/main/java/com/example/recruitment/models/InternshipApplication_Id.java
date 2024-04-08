package com.example.recruitment.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InternshipApplication_Id implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Column(name = "internship_id")
    private Long internshipId;
    @Column(name = "candidate_id")
    private Long candidateId;


}
