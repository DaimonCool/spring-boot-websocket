package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.dto.JoinChatDTO;
import com.dashko.spring.ws.api.model.MessageType;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import static com.dashko.spring.ws.api.ChatApplicationConstants.SessionAttributeConstants.*;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @Value("${chat.default.page}")
    private int page;

    @Value("${chat.default.number-messages}")
    private int numberOfMessages;

    @MessageMapping("/chat.register/{id}")
    @SendTo("/topic/public/{id}")
    public JoinChatDTO register(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor,
                                @DestinationVariable Long id) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put(USERNAME, chatMessageDTO.getSender());
        headerAccessor.getSessionAttributes().put(CHAT_ID, id);
        return messageService.joinChat(chatMessageDTO, id, page, numberOfMessages);
    }

    @MessageMapping("/chat.send/{id}")
    @SendTo("/topic/public/{id}")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO, @DestinationVariable Long id) {
        if (!chatMessageDTO.getType().equals(MessageType.TYPING)) {
            messageService.saveMessage(chatMessageDTO, id);
        }
        return chatMessageDTO;
    }

}
