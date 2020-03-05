package com.dashko.spring.ws.api.controller;

import com.dashko.spring.ws.api.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentsController {

    private final DocumentService documentService;

    @PostMapping
    public String uploadFile(@RequestParam("document") MultipartFile multipartFile) throws IOException {
        return documentService.uploadFile(multipartFile);
    }

    @GetMapping
    public List<String> getAllDocumentLinks() {
        return documentService.getAllDocumentLinks();
    }
}
