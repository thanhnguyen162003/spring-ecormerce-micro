package com.app.productservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID id;
    
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    private Double price;
    
    @Column(name = "product_slug", nullable = false)
    private String productSlug;

    @Column(name = "product_img", nullable = true)
    private String productImg;

    @Column(name = "product_markdown", nullable = true)
    private String productMarkdown;

    @Column(name = "deleted_at", nullable = true)
    private java.time.LocalDateTime deletedAt;
    
    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

} 