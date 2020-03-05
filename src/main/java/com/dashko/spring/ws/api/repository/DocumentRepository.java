package com.dashko.spring.ws.api.repository;

import com.dashko.spring.ws.api.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
