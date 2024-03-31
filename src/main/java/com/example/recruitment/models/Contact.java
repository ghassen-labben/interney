package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {
    private String rue;
    private String codePostal;
    private String ville;
    private String pays;
    private String addressComplete; // Added this line
    private String telephone;
    private String whatsapp;
    private String linkedin;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne(mappedBy = "contact", fetch = FetchType.LAZY)
    private Profile profile;

    public Contact(String rue, String codePostal, String ville, String pays, String addressComplete, String telephone, String whatsapp, String linkedin) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
        this.addressComplete = addressComplete;
        this.telephone = telephone;
        this.whatsapp = whatsapp;
        this.linkedin = linkedin;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(rue, contact.rue) &&
                Objects.equals(codePostal, contact.codePostal) &&
                Objects.equals(ville, contact.ville) &&
                Objects.equals(pays, contact.pays) &&
                Objects.equals(addressComplete, contact.addressComplete) &&
                Objects.equals(telephone, contact.telephone) &&
                Objects.equals(whatsapp, contact.whatsapp) &&
                Objects.equals(linkedin, contact.linkedin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rue, codePostal, ville, pays, addressComplete, telephone, whatsapp, linkedin);
    }

}