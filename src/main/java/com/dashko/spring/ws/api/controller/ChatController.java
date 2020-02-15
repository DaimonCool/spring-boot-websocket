package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.Message;
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.dto.JoinChatDTO;
import com.dashko.spring.ws.api.model.dto.MessageType;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private List<ChatMessageDTO> messages = new ArrayList<>();

    @MessageMapping("/chat.register/{id}")
    @SendTo("/topic/public/{id}")
    public JoinChatDTO register(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor,
                                @DestinationVariable String id) {
        headerAccessor.getSessionAttributes().put("username", chatMessageDTO.getSender());
        messageService.saveMessage(chatMessageDTO, Long.parseLong(id));
        messages.add(chatMessageDTO);
        return new JoinChatDTO(chatMessageDTO.getSender(), chatMessageDTO.getType(), messages);
    }

    @MessageMapping("/chat.send/{id}")
    @SendTo("/topic/public/{id}")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO, @DestinationVariable String id) {
        if (!chatMessageDTO.getType().equals(MessageType.TYPING)) {
            messageService.saveMessage(chatMessageDTO, Long.parseLong(id));
            messages.add(chatMessageDTO);
        }
        return chatMessageDTO;
    }

}
