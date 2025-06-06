package com.app.userservice.Controllers;

import com.app.userservice.Common.Mapper.UserMapper;
import com.app.userservice.Entities.User;
import com.app.userservice.Models.Request.UserRequest;
import com.app.userservice.Models.Response.ApiResponseModel;
import com.app.userservice.Models.Response.UserResponse;
import com.app.userservice.Security.JwtTokenUtil;
import com.app.userservice.Security.SecurityUtils;
import com.app.userservice.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "APIs for managing users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    private final SecurityUtils securityUtils;

    private final UserMapper userMapper;
    
    private final JwtTokenUtil jwtTokenUtil;
    
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    @PostMapping
    public ResponseEntity<ApiResponseModel<UserResponse>> createUser(
            @Parameter(description = "User object to create", required = true)
            @RequestBody UserRequest user) {
        User newUser = userMapper.toEntity(user);
        User createdUser = userService.createUser(newUser);
        return new ResponseEntity<>(
                ApiResponseModel.success("User created successfully", UserResponse.fromEntity(createdUser)),
            HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Get current user", description = "Returns the current authenticated user's information from JWT token")
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponseModel<UserResponse>> getCurrentUser() {
        return securityUtils.getCurrentToken()
                .map(token -> {
                    UserResponse userResponse = UserResponse.fromEntity(userService.getUserById(jwtTokenUtil.extractUserId(token)));
                    return ResponseEntity.ok(new ApiResponseModel<>(true, "User information retrieved from token", userResponse));
                })
                .orElse(ResponseEntity.badRequest().body(new ApiResponseModel<>(false, "No valid token found", null)));
    }
    
    @Operation(summary = "Get user by ID", description = "Returns user information by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseModel<UserResponse>> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponseModel.success(UserResponse.fromEntity(user)));
    }
    
    @Operation(summary = "Get all users with pagination", description = "Retrieves a paginated list of users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseModel<Page<UserResponse>>> getAllUsers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<User> users = userService.getAllUsersWithPagination(pageRequest);
        Page<UserResponse> userResponses = users.map(UserResponse::fromEntity);
        return ResponseEntity.ok(ApiResponseModel.success(userResponses));
    }
    
    @Operation(summary = "Update a user", description = "Updates an existing user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseModel<UserResponse>> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated user object", required = true)
            @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponseModel.success(UserResponse.fromEntity(updatedUser)));
    }
    
    @Operation(summary = "Delete a user", description = "Soft deletes a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseModel<Void>> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponseModel.success("User deleted successfully", null));
    }
    
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseModel<UserResponse>> getUserByEmail(
            @Parameter(description = "Email of the user to retrieve", required = true)
            @PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(ApiResponseModel.success(UserResponse.fromEntity(user)));
    }
    
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponseModel<UserResponse>> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve", required = true)
            @PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponseModel.success(UserResponse.fromEntity(user)));
    }
} 