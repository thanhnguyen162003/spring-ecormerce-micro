package com.app.userservice.Services.Interfaces;

import com.app.userservice.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {
    User createUser(User user);
    User getUserById(UUID id);
    Page<User> getAllUsersWithPagination(Pageable pageable);
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
    User findByEmail(String email);
    User findByUsername(String username);
} 