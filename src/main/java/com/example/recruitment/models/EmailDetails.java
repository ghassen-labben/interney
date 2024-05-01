package com.example.recruitment.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
}