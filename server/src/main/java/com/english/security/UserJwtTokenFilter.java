package com.english.security;

import com.english.enums.Role;
import com.english.exceptions.AuthenticationException;
import com.english.service.interfeices.CheckUserRegisteredService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserJwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CheckUserRegisteredService checkUserRegisteredService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AuthenticationException("Missing or invalid Authorization header");
            }

            String jwt = authHeader.substring(7);

            if (!jwtUtil.validateToken(jwt)) {
                throw new AuthenticationException("Invalid or expired token");
            }

            String login = jwtUtil.extractLogin(jwt);
            boolean isUserRegistered = checkUserRegisteredService.isUserRegistered(login);

            if (login == null || !isUserRegistered) {
                throw new AuthenticationException("User not registered or not found");
            }

            Role role = jwtUtil.extractRoles(jwt);
            Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(login, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
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
        String path = request.getServletPath();
        return path.startsWith("/api/users/register") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/error") ||
                path.startsWith("/api/creator/generate_admin_token") ||
                path.startsWith("/api/admin/register");
    }
}