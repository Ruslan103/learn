package com.english.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class RegisteringUserInfo {
    private String name;
    private String surname;
    private String login;
    private String secretKey;
}
