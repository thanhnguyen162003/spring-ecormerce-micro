package com.app.productservice.Services;

import com.app.productservice.Common.CustomModels.ResponseModel;
import com.app.productservice.Common.Mapper.ProductMapper;
import com.app.productservice.Models.Response.ProductResponse;
import com.app.productservice.Persistence.Interfaces.IProductRepository;
import com.app.productservice.Services.Interfaces.IProductService;
import com.app.productservice.Entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Autowired
    public ProductService(IProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findProductById(id);
        return product != null ? productMapper.toResponse(product) : null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsWithPagination(Pageable pageable) {
        return productRepository.findAllWithPagination(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, 
                  isolation = Isolation.READ_COMMITTED,
                  rollbackFor = {Exception.class})
    public ProductResponse createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                  isolation = Isolation.READ_COMMITTED,
                  rollbackFor = {Exception.class})
    public ProductResponse updateProduct(UUID id, Product product) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            product.setId(id);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toResponse(updatedProduct);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                  isolation = Isolation.READ_COMMITTED,
                  rollbackFor = {Exception.class})
    public ResponseModel deleteProduct(UUID id) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            existingProduct.setDeletedAt(java.time.LocalDateTime.now());
            productRepository.save(existingProduct);
            return new ResponseModel(HttpStatus.OK, "Delete Ok", id);
        }
        return new ResponseModel(HttpStatus.BAD_REQUEST, "Delete fail", null);
    }
}
