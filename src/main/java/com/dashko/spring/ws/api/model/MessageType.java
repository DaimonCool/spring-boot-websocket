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
public class MessageType {

    @Id
    @SequenceGenerator(sequenceName = "message_type_id_seq", name = "message_type_seq_gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_type_seq_gen")
    private long id;

    private String typeName;
}
