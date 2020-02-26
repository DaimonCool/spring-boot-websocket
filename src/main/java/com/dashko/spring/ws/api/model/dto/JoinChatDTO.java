package com.dashko.spring.ws.api.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinChatDTO {
    private String sender;
    private MessageTypeEnum type;
    private String content;
    private List<ChatMessageDTO> messages;

}
