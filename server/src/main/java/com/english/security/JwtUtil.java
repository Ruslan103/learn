package com.english.security;

import com.english.enums.Role;
import com.english.model.RegisteringUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(RegisteringUserInfo registeringUserInfo, Role role) {
        String name = registeringUserInfo.getName();
        String surname = registeringUserInfo.getSurname();
        String login = registeringUserInfo.getLogin();
        String tokenKey = registeringUserInfo.getSecretKey();
        return Jwts.builder()
                .claim("ROLE_", role.name())
                .claim("NAME_", name)
                .claim("SURNAME_", surname)
                .claim("TOKEN_KEY_", tokenKey)
                .claim("LOGIN_", login)
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateRegisterAdminToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            Claims claims = claimsJws.getPayload();
            return Role.ROLE_CREATOR.name().equals(claims.getSubject()) &&
                    claims.containsKey("ROLE_") &&
                    isCreatorRolePresent(Collections.singletonList(claims.get("ROLE_", String.class)));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCreatorRolePresent(List<?> roles) {
        return roles != null && roles.stream()
                .anyMatch(role -> role.toString().contains(Role.ROLE_CREATOR.name()));
    }

    public String generateTokenForRegisterAdmin() {
        return Jwts.builder()
                .subject(Role.ROLE_CREATOR.name())
                .claim("ROLE_", Role.ROLE_CREATOR.name())
                .signWith(getSecretKey())
                .compact();
    }

    public String extractLogin(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("LOGIN_", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Role extractRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object rolesObj = claims.get("ROLE_");
        String roleStr = String.valueOf(rolesObj);

        if (!Role.isRole(roleStr)) {
            return Role.ROLE_GUEST;
        }

        return Role.toRole(roleStr);
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}