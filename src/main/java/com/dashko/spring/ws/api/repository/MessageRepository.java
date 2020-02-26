package com.dashko.spring.ws.api.repository;

import com.dashko.spring.ws.api.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatIdOrderBySendDateDesc(long chatId, Pageable pageWithMessages);
}
