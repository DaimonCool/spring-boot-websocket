package com.dashko.spring.ws.api.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat {

    @Id
    @SequenceGenerator(sequenceName = "chat_id_seq", name = "chat_seq_gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq_gen")
    private long id;

    private String name;

    public Chat(long id) {
        this.id = id;
    }
}
