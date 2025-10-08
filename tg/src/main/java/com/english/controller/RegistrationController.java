package com.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("english/bot/api")
@RequiredArgsConstructor
public class RegistrationController {
    @PostMapping("/register")
    public String generateToken(@RequestParam String key) {
return "f";
    }
}
