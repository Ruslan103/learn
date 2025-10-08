package com.english.controller;

import com.english.model.RegisteringUserInfo;
import com.english.service.interfeices.RegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    private final RegistrationService registrationService;

    public AdminController(@Qualifier("adminRegistrationService") RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")

    public String generateToken(@RequestBody RegisteringUserInfo registeringUser) {
        return registrationService.register(registeringUser);
    }
}
