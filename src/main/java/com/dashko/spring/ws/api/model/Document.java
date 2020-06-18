package com.dashko.spring.ws.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Document {

    @Id
    @SequenceGenerator(sequenceName = "document_id_seq", name = "documents_seq_gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documents_seq_gen")
    private long id;

    private String link;

    public Document(String link) {
        this.link = link;
    }
}
