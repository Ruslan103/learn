package com.english.controller;

import com.english.model.RegisteringUserInfo;
import com.english.service.interfeices.CheckUserRegisteredService;
import com.english.service.interfeices.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final RegistrationService userRegistrationService;
    private final CheckUserRegisteredService checkUserRegisteredService;

    @PostMapping("/register")
    public String generateToken(@RequestBody RegisteringUserInfo registeringUser) {
        return userRegistrationService.register(registeringUser);
    }

    @GetMapping("/check-register")
    public boolean isUserRegister(@RequestParam String login) {
        return checkUserRegisteredService.isUserRegistered(login);
    }
}
