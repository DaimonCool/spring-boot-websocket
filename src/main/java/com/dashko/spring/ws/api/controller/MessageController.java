package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.model.dto.ChatMessageDTO;
import com.dashko.spring.ws.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/chat/{id}/messages")
    public List<ChatMessageDTO> getMessages(@PathVariable int id,
                                            @RequestParam int page,
                                            @RequestParam int messagesNum) {
        return messageService.getLastMessages(id, page, messagesNum);
    }
}
