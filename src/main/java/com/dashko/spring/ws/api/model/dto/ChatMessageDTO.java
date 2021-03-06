package com.dashko.spring.ws.api.model.dto;

import com.dashko.spring.ws.api.model.Message;
import com.dashko.spring.ws.api.model.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ChatMessageDTO {
    private String messageValue;
    private String sender;
    private LocalDateTime sendDate;
    private MessageType type;

    public static ChatMessageDTO from(Message message) {
        val chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setSender(message.getSender());
        chatMessageDTO.setMessageValue(message.getMessageValue());
        chatMessageDTO.setType(message.getMessageType());
        chatMessageDTO.setSendDate(message.getSendDate());
        return chatMessageDTO;
    }
}
