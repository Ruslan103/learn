package com.english.service;


import com.english.enums.Role;
import com.english.model.RegisteringUserInfo;
import com.english.model.User;
import com.english.security.JwtUtil;
import com.english.service.interfeices.RegistrationService;
import com.english.service.interfeices.TokenKeyGenerator;
import com.english.service.interfeices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService implements RegistrationService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisteringUserInfo registeringUserInfo) {
        String name = registeringUserInfo.getName();
        String surname = registeringUserInfo.getSurname();
        String login = registeringUserInfo.getLogin();

        User user = User.builder()
                .login(login)
                .name(name)
                .surname(surname)
                .role(Role.ROLE_USER)
                .build();

        userService.saveUser(user);
        return jwtUtil.generateToken(registeringUserInfo, Role.ROLE_USER);
    }
}
