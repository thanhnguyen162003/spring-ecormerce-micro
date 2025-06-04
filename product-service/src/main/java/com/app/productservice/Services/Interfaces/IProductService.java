package com.app.productservice.Services.Interfaces;

import com.app.productservice.Common.CustomModels.ResponseModel;
import com.app.productservice.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    // Create
    Product createProduct(Product product);
    
    // Read
    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Page<Product> getAllProductsWithPagination(Pageable pageable);
    
    // Update
    Product updateProduct(UUID id, Product product);
    
    // Delete
    ResponseModel deleteProduct(UUID id);
}
