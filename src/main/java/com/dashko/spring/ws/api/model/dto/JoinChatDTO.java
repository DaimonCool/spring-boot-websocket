package com.dashko.spring.ws.api.model.dto;

import com.dashko.spring.ws.api.model.MessageType;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinChatDTO {
    private String sender;
    private MessageType type;
    private String content;
    private List<ChatMessageDTO> messages;

}
