package com.dashko.spring.ws.api.repository;

import com.dashko.spring.ws.api.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
