package com.dashko.spring.ws.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PageController {

    @GetMapping("/chats")
    public String getMainPage(){
        return "index";
    }

    @GetMapping("/documents")
    public String getFilesPage(){
        return "documents";
    }

    @GetMapping("/calls")
    public String getCallerPage(){
        return "caller";
    }

}
