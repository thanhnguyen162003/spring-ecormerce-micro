package com.app.productservice.Services;

import com.app.productservice.Common.CustomModels.ResponseModel;
import com.app.productservice.Persistence.Interfaces.IProductRepository;
import com.app.productservice.Services.Interfaces.IProductService;
import com.app.productservice.Entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    
    @Autowired
    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(UUID id) {
        return productRepository.findProductById(id);
    }
    
    @Override
    public Page<Product> getAllProductsWithPagination(Pageable pageable) {
        return productRepository.findAllWithPagination(pageable);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            product.setId(id); // Ensure the ID is set
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public ResponseModel deleteProduct(UUID id) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            existingProduct.setDeletedAt(java.time.LocalDateTime.now());
            productRepository.save(existingProduct);
            return new ResponseModel(HttpStatus.OK, "Delete Ok", id );
        }
        return new ResponseModel(HttpStatus.BAD_REQUEST, "Delete fail", null );
    }
}
