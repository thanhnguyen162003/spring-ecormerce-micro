package com.app.userservice.grpc;

import com.app.userservice.Entities.User;
import com.app.userservice.Services.UserService;
import com.app.userservice.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@GrpcService
@Service
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            User user = userService.getUserById(UUID.fromString(request.getUserId()));
            UserResponse response = mapUserToResponse(user);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void validateUser(ValidateUserRequest request, StreamObserver<UserValidationResponse> responseObserver) {
        try {
            User user = userService.getUserById(UUID.fromString(request.getUserId()));
            
            UserValidationResponse response = UserValidationResponse.newBuilder()
                .setIsValid(user.isActive())
                .setMessage(user.isActive() ? "User is valid" : "User is inactive")
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            UserValidationResponse response = UserValidationResponse.newBuilder()
                .setIsValid(false)
                .setMessage("User validation failed: " + e.getMessage())
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private UserResponse mapUserToResponse(User user) {
        return UserResponse.newBuilder()
            .setId(user.getId().toString())
            .setUsername(user.getUsername())
            .setEmail(user.getEmail())
            .setFirstName(user.getFirstName() != null ? user.getFirstName() : "")
            .setLastName(user.getLastName() != null ? user.getLastName() : "")
            .setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : "")
            .setRole(user.getRole())
            .setIsActive(user.isActive())
            .setCreatedAt(user.getCreatedAt().format(formatter))
            .setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : "")
            .build();
    }
} 