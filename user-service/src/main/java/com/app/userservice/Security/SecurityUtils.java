package com.app.userservice.Security;

import com.app.userservice.Entities.User;
import com.app.userservice.Persistence.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

    public Optional<UUID> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    public Optional<String> getCurrentUserEmail() {
        return getCurrentUser().map(User::getEmail);
    }

    public Optional<String> getCurrentUsername() {
        return getCurrentUser().map(User::getUsername);
    }
} 