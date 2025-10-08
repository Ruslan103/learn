package com.english.controller;

import com.english.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {
    private final JwtUtil jwtUtil;

    @GetMapping("/generate_admin_token")
    public String generateToken() {
        return jwtUtil.generateTokenForRegisterAdmin();
    }
}
