package com.dashko.spring.ws.api.service;

import com.dashko.spring.ws.api.model.Document;
import com.dashko.spring.ws.api.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3Service s3Service;
    private final DocumentRepository documentRepository;

    @Transactional
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        val fileLink = s3Service.uploadFile(multipartFile);
        val document = new Document(fileLink);
        documentRepository.save(document);
        return fileLink;
    }

    @Transactional(readOnly = true)
    public List<String> getAllDocumentLinks() {
        return documentRepository.findAll()
                .stream()
                .map(Document::getLink)
                .collect(Collectors.toList());
    }
}
