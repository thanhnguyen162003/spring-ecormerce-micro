package com.app.userservice.Services;

import com.app.userservice.Entities.User;
import com.app.userservice.Persistence.Repository.UserRepository;
import com.app.userservice.Services.Interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void validateRole(String role) {
        if (role == null || (!role.equals("ADMIN") && !role.equals("USER"))) {
            throw new IllegalArgumentException("Role must be either ADMIN or USER");
        }
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        validateRole(user.getRole());
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Page<User> getAllUsersWithPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User updateUser(UUID id, User user) {
        User existingUser = getUserById(id);
        
        // Check if email is being changed and if it's already taken
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Check if username is being changed and if it's already taken
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Validate role if it's being changed
        if (user.getRole() != null && !user.getRole().equals(existingUser.getRole())) {
            validateRole(user.getRole());
        }

        // Update fields
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        
        // Only encode and update password if it's being changed
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        existingUser.setActive(user.isActive());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
} 