package com.dashko.spring.ws.api.listener;

import com.dashko.spring.ws.api.model.MessageType;
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static com.dashko.spring.ws.api.ChatApplicationConstants.SessionAttributeConstants.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get(USERNAME);
        String chatId = (String) headerAccessor.getSessionAttributes().get(CHAT_ID);
        if (username != null) {
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setType(MessageType.LEAVE);
            chatMessageDTO.setSender(username);
            chatMessageDTO.setSendDate(LocalDateTime.now(ZoneOffset.UTC));
            chatMessageDTO.setMessageValue(username + " left!");
            messageService.saveMessage(chatMessageDTO, Long.parseLong(chatId));
            messagingTemplate.convertAndSend("/topic/public/" + chatId, chatMessageDTO);
        }
    }
}