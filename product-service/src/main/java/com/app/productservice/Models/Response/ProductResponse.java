package com.app.productservice.Models.Response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String slug;
    private String image;
    private String markdown;
    private LocalDateTime createdAt;
} 