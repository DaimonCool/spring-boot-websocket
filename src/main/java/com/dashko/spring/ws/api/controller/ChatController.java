package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.ChatMessage;
import com.dashko.spring.ws.api.model.JoinChat;
import com.dashko.spring.ws.api.model.MessageType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private List<ChatMessage> messages = new ArrayList<>();

    @MessageMapping("/chat.register/{id}")
    @SendTo("/topic/public/{id}")
    public JoinChat register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor,
                             @DestinationVariable String id) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messages.add(chatMessage);
        return new JoinChat(chatMessage.getSender(), chatMessage.getType(), messages);
    }

    @MessageMapping("/chat.send/{id}")
    @SendTo("/topic/public/{id}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String id) {
        if (!chatMessage.getType().equals(MessageType.TYPING)) {
            messages.add(chatMessage);
        }
        return chatMessage;
    }

}
