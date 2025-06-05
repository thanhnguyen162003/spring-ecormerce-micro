package com.app.productservice.Models.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotBlank(message = "Product description is required")
    private String description;
    
    @NotNull(message = "Product price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    
    @NotNull(message = "Product quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
} 