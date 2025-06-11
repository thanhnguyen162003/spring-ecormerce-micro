package com.app.productservice.Services.Interfaces;

import com.app.productservice.Common.CustomModels.ResponseModel;
import com.app.productservice.Models.Response.ProductResponse;
import com.app.productservice.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IProductService {
    // Create
    ProductResponse createProduct(Product product);
    
    // Read
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(UUID id);
    Product getProductEntityById(UUID id);
    Page<ProductResponse> getAllProductsWithPagination(Pageable pageable);
    
    // Update
    CompletableFuture<ProductResponse> updateProduct(UUID id, Product product);
    
    // Delete
    CompletableFuture<ResponseModel> deleteProduct(UUID id);
}
