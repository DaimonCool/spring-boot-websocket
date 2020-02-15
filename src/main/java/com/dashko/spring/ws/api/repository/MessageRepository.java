package com.dashko.spring.ws.api.repository;

import com.dashko.spring.ws.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
