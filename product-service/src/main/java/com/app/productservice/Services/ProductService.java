package com.app.productservice.Services;

import com.app.productservice.Persistence.Interfaces.IProductRepository;
import com.app.productservice.Services.Interfaces.IProductService;
import com.app.productservice.Entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Product getProductById(Long id) {
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
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            product.setId(id); // Ensure the ID is set
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            existingProduct.setDeletedAt(java.time.LocalDateTime.now());
            productRepository.save(existingProduct);
        }
    }
}
