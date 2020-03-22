package com.example.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    @NotEmpty
    @Column(name = "doc_name")
    private String docName;

    @NotEmpty
    @Column(name = "file")
    @Lob
    private byte[] file;
}
