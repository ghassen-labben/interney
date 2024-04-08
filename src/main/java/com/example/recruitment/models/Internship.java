package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp; // Correct import
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Internship implements Serializable {
    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    @ManyToMany
    @JoinTable(
            name = "internship_skills",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();



    private Date deadline;
    private Date startDate;
    private Date endDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @JsonBackReference
    @OneToMany(mappedBy = "internship")
    Set<InternshipApplication> internshipApplications;

    @CreationTimestamp
    private Timestamp regdate;

    @UpdateTimestamp
    private Timestamp updatedate;





    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public Internship(String title, String description, List<Skill> skills, Date deadline, Date startDate, Date endDate, Department department, Timestamp regdate, Timestamp updatedate) {
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
        this.regdate = regdate;
        this.updatedate = updatedate;
    }
}
