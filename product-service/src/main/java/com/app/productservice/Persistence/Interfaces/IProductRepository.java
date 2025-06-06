package com.app.productservice.Persistence.Interfaces;

import com.app.productservice.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
    
    @Query(value = "SELECT * FROM products ORDER BY created_at DESC",
           countQuery = "SELECT COUNT(*) FROM products",
           nativeQuery = true)
    Page<Product> findAllWithPagination(Pageable pageable);
    
    @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
    Product findProductById(@Param("id") UUID id);
} 