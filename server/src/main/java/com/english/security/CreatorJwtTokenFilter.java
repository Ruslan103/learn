package com.english.security;

import com.english.exceptions.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatorJwtTokenFilter extends OncePerRequestFilter {
    @Value("${creator.role.login}")
    private String login;
    @Value("${creator.role.password}")
    private String password;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {

        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
                throw new AuthenticationException("Authorization header is required");
            }

            String base64Credentials = authorizationHeader.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) {
                throw new AuthenticationException("Invalid Basic Auth format");
            }

            String requestLogin = parts[0];
            String requestPassword = parts[1];

            if (!login.equals(requestLogin) || !password.equals(requestPassword)) {
                throw new AuthenticationException("Invalid login or password");
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {
            log.error("Authentication error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorMessage =  "{\"error\": \"" + e.getMessage() + "\"}";
            response.getWriter().write(errorMessage);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/creator/generate_admin_token");
    }
}
