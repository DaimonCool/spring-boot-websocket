package com.dashko.spring.ws.api.repository;

import com.dashko.spring.ws.api.model.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTypeRepository extends JpaRepository<MessageType, Long> {

    MessageType findByTypeNameIgnoreCase(String typeName);
}
