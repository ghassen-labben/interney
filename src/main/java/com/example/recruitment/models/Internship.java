package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp; // Correct import
import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    @Lob
    private String skills;

    @Lob
    private String details;

    private Date startDate;
    private Date endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @CreationTimestamp
    private Timestamp regdate;

    @UpdateTimestamp
    private Timestamp updatedate;

    public Internship(String title, String description, String skills, Date startDate,String details, Date endDate) {
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.details=details;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
