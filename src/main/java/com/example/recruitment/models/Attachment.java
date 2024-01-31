package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(generator ="uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    private String id;

    public Attachment(String fileName, String fileType, String filePath) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    private String fileName;
    private String fileType;

    @Column(name = "filePath", nullable = false)
    private String filePath;



}
