package com.dashko.spring.ws.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinChatDTO {
    private String sender;
    private MessageTypeEnum type;
    private List<ChatMessageDTO> messages;

}
