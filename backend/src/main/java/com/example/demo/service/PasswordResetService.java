package com.example.demo.service;

import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${password.reset.token.expiration}")
    private long tokenExpirationMs;
    
    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        
        // Set token and expiry
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusSeconds(tokenExpirationMs / 1000));
        
        userRepository.save(user);
        
        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        
        log.info("Password reset token generated for user: {}", user.getEmail());
        
        return MessageResponse.builder()
                .message("Password reset email has been sent to " + request.getEmail())
                .build();
    }
    
    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        log.info("Password reset attempt with token: {}", request.getToken());
        
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));
        
        // Check if token is expired
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Reset token has expired");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        
        // Clear reset token
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        
        userRepository.save(user);
        
        log.info("Password reset successful for user: {}", user.getEmail());
        
        return MessageResponse.builder()
                .message("Password has been reset successfully")
                .build();
    }
}