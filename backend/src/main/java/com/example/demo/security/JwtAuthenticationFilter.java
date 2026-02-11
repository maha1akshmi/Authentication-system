package com.example.demo.security;


import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.userdetails.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        log.info("=== JWT Filter Started ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Auth Header: {}", authHeader);
        
        // Check if Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No valid Authorization header found");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Extract JWT token
            jwt = authHeader.substring(7);
            log.info("Extracted JWT: {}", jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
            
            userEmail = jwtService.extractUsername(jwt);
            log.info("Extracted email from token: {}", userEmail);
            
            // Validate token and set authentication
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Validating token for user: {}", userEmail);
                
                // Create a simple UserDetails object with just the username
                org.springframework.security.core.userdetails.UserDetails userDetails = User.builder()
                        .username(userEmail)
                        .password("")
                        .roles("USER")
                        .build();
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            null
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.info("✅ Authentication set successfully for user: {}", userEmail);
                } else {
                    log.error("❌ Token validation failed for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("❌ Error in JWT filter: {}", e.getMessage(), e);
        }
        
        filterChain.doFilter(request, response);
    }
}