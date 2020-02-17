package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.dto.JoinChatDTO;
import com.dashko.spring.ws.api.model.dto.MessageTypeEnum;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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

    @MessageMapping("/chat.register/{id}")
    @SendTo("/topic/public/{id}")
    private JoinChatDTO register(@Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor,
                                 @DestinationVariable String id) {
        headerAccessor.getSessionAttributes().put("username", chatMessageDTO.getSender());
        headerAccessor.getSessionAttributes().put("chatId", id);
        long chatId = Long.parseLong(id);
        messageService.saveMessage(chatMessageDTO, chatId);
        val messages = messageService.getLast30Messages(chatId);
        messages.add(chatMessageDTO);
        return new JoinChatDTO(chatMessageDTO.getSender(), chatMessageDTO.getType(), messages);
    }

    @MessageMapping("/chat.send/{id}")
    @SendTo("/topic/public/{id}")
    private ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO, @DestinationVariable String id) {
        if (!chatMessageDTO.getType().equals(MessageTypeEnum.TYPING)) {
            messageService.saveMessage(chatMessageDTO, Long.parseLong(id));
        }
        return chatMessageDTO;
    }

}
