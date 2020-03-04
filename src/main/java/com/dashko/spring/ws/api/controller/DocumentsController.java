package com.dashko.spring.ws.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
public class DocumentsController {

    @PostMapping
    public void uploadFile(@RequestParam("document") MultipartFile multipartFile) {
        System.out.println(multipartFile);
    }
}
