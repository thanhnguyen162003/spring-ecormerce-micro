package com.app.productservice.grpc;

import com.app.productservice.Entities.Product;
import com.app.productservice.Services.ProductService;
import com.app.productservice.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@GrpcService
@Service
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            Product product = productService.getProductEntityById(UUID.fromString(request.getProductId()));
            ProductResponse response = mapProductToResponse(product);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void validateProduct(ValidateProductRequest request, StreamObserver<ProductValidationResponse> responseObserver) {
        try {
            Product product = productService.getProductEntityById(UUID.fromString(request.getProductId()));
            
            ProductValidationResponse response = ProductValidationResponse.newBuilder()
                .setIsValid(true)
                .setMessage("Product is valid")
                .setPrice(product.getPrice())
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            ProductValidationResponse response = ProductValidationResponse.newBuilder()
                .setIsValid(false)
                .setMessage("Product validation failed: " + e.getMessage())
                .setPrice(0.0)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private ProductResponse mapProductToResponse(Product product) {
        return ProductResponse.newBuilder()
            .setId(product.getId().toString())
            .setName(product.getProductName())
            .setDescription(product.getProductDescription())
            .setPrice(product.getPrice())
            .setSlug(product.getProductSlug())
            .setImage(product.getProductImg() != null ? product.getProductImg() : "")
            .setMarkdown(product.getProductMarkdown() != null ? product.getProductMarkdown() : "")
            .setCreatedAt(product.getCreatedAt().format(formatter))
            .build();
    }
} 