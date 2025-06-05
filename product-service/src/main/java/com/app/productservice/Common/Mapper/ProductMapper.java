package com.app.productservice.Common.Mapper;

import com.app.productservice.Models.Request.ProductRequest;
import com.app.productservice.Models.Response.ProductResponse;
import com.app.productservice.Entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends BaseMapper {
    
    public ProductResponse toResponse(Product product) {
        return convertToDto(product, ProductResponse.class);
    }
    
    public Product toEntity(ProductRequest request) {
        return convertToEntity(request, Product.class);
    }
} 