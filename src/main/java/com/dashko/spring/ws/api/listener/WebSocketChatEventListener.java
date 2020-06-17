package com.dashko.spring.ws.api.listener;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.MessageType;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String chatId = (String) headerAccessor.getSessionAttributes().get("chatId");
        if (username != null) {
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setType(MessageType.LEAVE);
            chatMessageDTO.setSender(username);
            chatMessageDTO.setMessageValue(username + " left!");
            messageService.saveMessage(chatMessageDTO, Long.parseLong(chatId));
            messagingTemplate.convertAndSend("/topic/public/" + chatId, chatMessageDTO);
        }
    }
}