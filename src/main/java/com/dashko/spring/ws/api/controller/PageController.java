package com.dashko.spring.ws.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/main")
    public String getMainPage(){
        return "index.html";
    }
}
