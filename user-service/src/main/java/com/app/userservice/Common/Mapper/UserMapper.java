package com.app.userservice.Common.Mapper;

import com.app.userservice.Models.Request.UserRequest;
import com.app.userservice.Models.Response.UserResponse;
import com.app.userservice.Entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper {
    
    public UserResponse toResponse(User user) {
        return convertToDto(user, UserResponse.class);
    }
    
    public User toEntity(UserRequest request) {
        return convertToEntity(request, User.class);
    }
} 