package com.dashko.spring.ws.api.service;

import com.dashko.spring.ws.api.model.Message;
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.model.dto.JoinChatDTO;
import com.dashko.spring.ws.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public JoinChatDTO joinChat(ChatMessageDTO chatMessageDTO, long chatId, int page, int numMessages) {
        val messages = this.getLastMessages(chatId, page, numMessages);
        messages.add(chatMessageDTO);
        this.saveMessage(chatMessageDTO, chatId);
        return new JoinChatDTO(chatMessageDTO.getSender(),
                chatMessageDTO.getType(),
                chatMessageDTO.getMessageValue(),
                messages);
    }

    @Transactional
    @Async
    public void saveMessage(ChatMessageDTO chatMessageDTO, long chatId) {
        val message = Message.from(chatMessageDTO, chatId);
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
