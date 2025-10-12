package com.esercizio.milestone.ticket_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("")
public class RestApiController {

    @GetMapping("/ciao")
    public String getMethodName() {
        return "ciao";
    }
    
}
