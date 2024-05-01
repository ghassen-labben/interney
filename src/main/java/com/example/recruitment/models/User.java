package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_NAME", "EMAIL"})})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails, Serializable  {
    private static final long serialVersionUID = 9178661439383356177L;

    private static final int maxEncadramant = 5;

    @JsonBackReference
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Profile profile;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME",unique = true,nullable = false)
    private String username;

    @NotAudited
    @Column(name = "PASSWORD",nullable = false)
    private String password;

    @Column(name = "EMAIL",nullable = false,unique = true)
    private String email;

    private Boolean status;


    @JsonIgnore
    @Column(name = "ACCOUNT_EXPIRED")
    private boolean accountExpired;

    @JsonIgnore
    @Column(name = "ACCOUNT_LOCKED")
    private boolean accountLocked;

    @JsonIgnore
    @Column(name = "CREDENTIALS_EXPIRED")
    private boolean credentialsExpired;

    @JsonIgnore
    @Column(name = "ENABLED")
    private boolean enabled;

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"))
    @OrderBy
    private Collection<Authority> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
    Set<InternshipApplication> appliedInternships;

    public User(String username, String password, String email, Boolean status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Notification> notifications;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                '}';
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;


    @NotAudited
    @JsonIgnore
    @Max(value = 5, message = "An encadrant can only supervise a maximum of 5 internship applications")
    @OneToMany(mappedBy = "encadrant")
    private Set<InternshipApplication> applicationsSupervisees = new HashSet<>();

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public void addApplicationSupervisees(InternshipApplication internshipApplication) throws Exception {
        if(applicationsSupervisees.stream()
                .filter(InternshipApplication::isFullyAccepted)
                .count()>=maxEncadramant){
            throw new Exception("Encadreur saturated");
        }
        this.applicationsSupervisees.add(internshipApplication);
    }

    public User(String username, String password, String email, Collection<Authority> authorities, String createdBy) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.createdBy = createdBy;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
