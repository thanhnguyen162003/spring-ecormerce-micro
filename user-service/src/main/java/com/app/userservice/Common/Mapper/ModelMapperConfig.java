package com.app.userservice.Common.Mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.app.userservice.Entities.User;
import com.app.userservice.Models.Request.UserRequest;
import com.app.userservice.Models.Response.UserResponse;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure mapping from User to UserResponse
        modelMapper.addMappings(new PropertyMap<User, UserResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setEmail(source.getEmail());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setPhoneNumber(source.getPhoneNumber());
                map().setRoles(source.getRoles());
                map().setActive(source.isActive());
                map().setCreatedAt(source.getCreatedAt());
                map().setUpdatedAt(source.getUpdatedAt());
            }
        });
        
        // Configure mapping from UserRequest to User
        modelMapper.addMappings(new PropertyMap<UserRequest, User>() {
            @Override
            protected void configure() {
                map().setUsername(source.getUsername());
                map().setEmail(source.getEmail());
                map().setPassword(source.getPassword());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setPhoneNumber(source.getPhoneNumber());
                // Skip mapping fields that are not in the request
                skip().setId(null);
                skip().setRoles(null);
                skip().setActive(null);
                skip().setCreatedAt(null);
                skip().setUpdatedAt(null);
            }
        });
        
        return modelMapper;
    }
} 