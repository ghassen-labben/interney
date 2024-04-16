package com.example.recruitment.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InternshipApplication_Id implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Column(name = "internship_id")
    private Long internshipId;
    @Column(name = "candidate_id")
    private Long candidateId;

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, internshipId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InternshipApplication_Id other = (InternshipApplication_Id) obj;
        return Objects.equals(this.candidateId, other.candidateId) &&
                Objects.equals(this.internshipId, other.internshipId);
    }
}
