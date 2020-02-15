package com.dashko.spring.ws.api.service;

import com.dashko.spring.ws.api.model.Message;
import com.dashko.spring.ws.api.model.MessageType;
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.repository.MessageRepository;
import com.dashko.spring.ws.api.repository.MessageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageTypeRepository messageTypeRepository;

    @Transactional
    @Async
    public void saveMessage(ChatMessageDTO chatMessageDTO, long chatId) {
        Message message = Message.from(chatMessageDTO, chatId);
        MessageType messageType = messageTypeRepository.findByTypeNameIgnoreCase(chatMessageDTO.getType().toString());
        message.setMessageType(messageType);

        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        message.setSendDate(localDateTime);
        messageRepository.save(message);
    }
}
