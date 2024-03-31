package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Profile() {
        this.experiences = new ArrayList<>();
        this.educations = new ArrayList<>();
        this.cv = new Attachment();
        this.profileImage=new Attachment();
        this.skills = new ArrayList<>();
        this.contact=new Contact();
    }

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,unique = true)
    @JsonIgnoreProperties({"password", "accountExpired","accountLocked","credentialsExpired","enabled","id","accountNonExpired","accountNonLocked","credentialsNonExpired"})
    private User user;
    @JsonManagedReference
    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @JsonManagedReference
     @OneToMany( mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", nullable = true)
    private Contact contact;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn( name = "profile_image_id", nullable = true)
    private Attachment profileImage;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cv_id", nullable = true)
    private Attachment cv;
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



    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    // Add methods for managing attachments

    public void setProfileImage(Attachment profileImage) {
        if (this.profileImage != null) {
            this.profileImage.setProfile(null);
        }
        this.profileImage = profileImage;
        if (profileImage != null) {
            profileImage.setProfile(this);
        }
    }

    public void setCv(Attachment cv) {
        if (this.cv != null) {
            this.cv.setProfile(null);
        }
        this.cv = cv;
        if (cv != null) {
            cv.setProfile(this);
        }
    }

    // Add method for managing contact

    public void setContact(Contact contact) {
        if (this.contact != null) {
            this.contact.setProfile(null);
        }
        this.contact = contact;
        contact.setProfile(this);
    }

}