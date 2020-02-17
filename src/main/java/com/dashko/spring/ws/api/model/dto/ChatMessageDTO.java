package com.dashko.spring.ws.api.model.dto;

import com.dashko.spring.ws.api.model.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private String content;
    private String sender;
    private LocalDateTime sendDate;
    private MessageTypeEnum type;


    public static ChatMessageDTO from(Message message) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setSender(message.getSender());
        chatMessageDTO.setContent(message.getMessage());
        chatMessageDTO.setType(MessageTypeEnum.valueOf(message.getMessageType().getTypeName()));
        chatMessageDTO.setSendDate(message.getSendDate());
        return chatMessageDTO;
    }
}
