package com.example.recruitment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {
    private static final long serialVersionUID = 9178661439383356178L;

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", internships=" + internships +
                ", encadrants=" + encadrants +
                ", regdate=" + regdate +
                ", updatedate=" + updatedate +
                '}';
    }

    @Id
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<Internship> internships = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<User> encadrants = new HashSet<>();

    @CreationTimestamp
    private Timestamp regdate;

    @UpdateTimestamp
    private Timestamp updatedate;

    public Department(String name, Set<User> encadrants,Set<Internship> internships) {
        this.internships=internships;
        this.name = name;
        this.encadrants = encadrants;
    }

    public void addInternship(Internship internship) {
        internships.add(internship);
    }

    public void addEncadrant(User encadrant) {
        encadrants.add(encadrant);
    }
}
