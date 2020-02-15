package com.dashko.spring.ws.api.model;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @SequenceGenerator(sequenceName = "message_id_seq", name = "message_seq_gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq_gen")
    private long id;

    private String message;
    private String sender;
    private LocalDateTime sendDate;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "message_type_id")
    private MessageType messageType;

    public static Message from(ChatMessageDTO chatMessageDTO, long id) {
        Message message = new Message();
        message.setMessage(chatMessageDTO.getContent());
        message.setSender(chatMessageDTO.getSender());
        message.setChat(new Chat(id));
        return message;
    }
}
