package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "profile_skills",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

    public void removeExperience(Experience experience) {
        experiences.remove(experience);
    }



    public void addEducation(Education education) {
        educations.add(education);
    }

    public void removeEducation(Education education) {
        educations.remove(education);
    }

    public void setContact(Contact contact) {
        if (this.contact != null) {
            this.contact.setProfile(null);
        }
        this.contact = contact;
        contact.setProfile(this);
    }



}