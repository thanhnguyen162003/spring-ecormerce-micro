package com.app.productservice.Common.Mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.app.productservice.Entities.Product;
import com.app.productservice.Models.Request.ProductRequest;
import com.app.productservice.Models.Response.ProductResponse;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure mapping from Product to ProductResponse
        modelMapper.addMappings(new PropertyMap<Product, ProductResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getProductName());
                map().setDescription(source.getProductDescription());
                map().setPrice(source.getPrice());
                map().setSlug(source.getProductSlug());
                map().setImage(source.getProductImg());
                map().setMarkdown(source.getProductMarkdown());
                map().setCreatedAt(source.getCreatedAt());
            }
        });
        
        // Configure mapping from ProductRequest to Product
        modelMapper.addMappings(new PropertyMap<ProductRequest, Product>() {
            @Override
            protected void configure() {
                map().setProductName(source.getName());
                map().setProductDescription(source.getDescription());
                map().setPrice(source.getPrice());
                // Skip mapping fields that are not in the request
                skip().setId(null);
                skip().setProductSlug(null);
                skip().setProductImg(null);
                skip().setProductMarkdown(null);
                skip().setDeletedAt(null);
                skip().setCreatedAt(null);
            }
        });
        
        return modelMapper;
    }
} 