package com.example.recruitment.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(generator ="uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(nullable = false)
    private String attachmentType;
    public Attachment(String fileName, String fileType, String filePath, String attachmentType) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.attachmentType = attachmentType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
