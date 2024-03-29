package com.example.recruitment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
}