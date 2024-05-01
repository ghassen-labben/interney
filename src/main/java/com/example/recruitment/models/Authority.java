package com.example.recruitment.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "AUTHORITY")
@Getter
@AllArgsConstructor
@Setter
public class Authority implements GrantedAuthority ,Serializable {


    @NotNull
    @Id
    @Column(name = "id", length = 50) // Specify the column name
    private String authority;

    public Authority() {

    }




    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return Objects.equals(authority, ((Authority) o).authority);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authority);
    }

    @Override
    public String toString() {
        return getAuthority();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}