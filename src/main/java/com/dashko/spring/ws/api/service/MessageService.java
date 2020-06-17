package com.dashko.spring.ws.api.service;

import com.dashko.spring.ws.api.model.Message;
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.repository.ChatRepository;
import com.dashko.spring.ws.api.repository.MessageRepository;
import com.dashko.spring.ws.api.repository.MessageTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageTypeRepository messageTypeRepository;
    private final ChatRepository chatRepository;

    @Transactional
    @Async
    public void saveMessage(ChatMessageDTO chatMessageDTO, long chatId) {
        val message = Message.from(chatMessageDTO, chatId);
        val localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        message.setSendDate(localDateTime);

        val messageType = messageTypeRepository.findByTypeNameIgnoreCase(chatMessageDTO.getType().toString());
        message.setMessageType(messageType);

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getLastMessages(long chatId, int page, int numMessages) {
        val pageWithMessages = PageRequest.of(page, numMessages);
        val messages = messageRepository.findAllByChatIdOrderBySendDateDesc(chatId, pageWithMessages);
        return messages.stream()
                .sorted(Comparator.comparing(Message::getSendDate))
                .map(ChatMessageDTO::from)
                .collect(Collectors.toList());
    }
}
