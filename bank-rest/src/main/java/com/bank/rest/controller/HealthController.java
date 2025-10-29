package com.bank.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Bank REST API is running!";
    }

    @GetMapping("/checking")
    public String health() {
        return "OK";
    }
}

