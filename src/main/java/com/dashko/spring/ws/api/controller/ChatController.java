package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.dto.JoinChatDTO;
import com.dashko.spring.ws.api.model.MessageType;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

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
        val chatId = Long.parseLong(id);

        val messages = messageService.getLastMessages(chatId, 0, 30);
        messages.add(chatMessageDTO);
        messageService.saveMessage(chatMessageDTO, chatId);

        return new JoinChatDTO(chatMessageDTO.getSender(),
                chatMessageDTO.getType(),
                chatMessageDTO.getContent(),
                messages);
    }

    @MessageMapping("/chat.send/{id}")
    @SendTo("/topic/public/{id}")
    private ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO, @DestinationVariable String id) {
        if (!chatMessageDTO.getType().equals(MessageType.TYPING)) {
            messageService.saveMessage(chatMessageDTO, Long.parseLong(id));
        }
        return chatMessageDTO;
    }

}
