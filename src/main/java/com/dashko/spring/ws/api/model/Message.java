package com.dashko.spring.ws.api.model;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @SequenceGenerator(sequenceName = "message_id_seq", name = "message_seq_gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq_gen")
    private long id;

    private String messageValue;
    private String sender;
    private LocalDateTime sendDate;

    @ManyToOne
    private Chat chat;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    public static Message from(ChatMessageDTO chatMessageDTO, long id) {
        Message message = new Message();
        message.setMessageValue(chatMessageDTO.getMessageValue());
        message.setMessageType(chatMessageDTO.getType());
        message.setSender(chatMessageDTO.getSender());
        message.setSendDate(chatMessageDTO.getSendDate());
        message.setChat(new Chat(id));
        return message;
    }
}
