package com.app.productservice.Controllers;

import com.app.productservice.Common.CustomModels.ResponseModel;
import com.app.productservice.Common.Mapper.ProductMapper;
import com.app.productservice.Entities.Product;
import com.app.productservice.Models.Request.ProductRequest;
import com.app.productservice.Models.Response.ProductResponse;
import com.app.productservice.Services.ProductService;
import com.app.productservice.Common.Pagination.PaginationHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "APIs for managing products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Create a new product", description = "Creates a new product in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Product object to create", required = true)
            @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        ProductResponse createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //Apply Resiliency
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "product")
    @Retry(name = "product")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable UUID id) {
        ProductResponse product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    public CompletableFuture<String> fallbackMethod(@PathVariable UUID id, RuntimeException runtimeException) {
        log.info("Cannot Get Product Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
    
    @Operation(summary = "Get all products with pagination", description = "Retrieves a paginated list of products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "created_at") String sortBy) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ProductResponse> productPage = productService.getAllProductsWithPagination(pageRequest);
        
        PaginationHeader paginationHeader = new PaginationHeader(
            productPage.getNumber(),
            productPage.getSize(),
            productPage.getTotalPages(),
            productPage.getTotalElements()
        );
        String paginationJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            paginationJson = objectMapper.writeValueAsString(paginationHeader);
        } catch (JsonProcessingException e) {
            // fallback: empty json
            paginationJson = "{}";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Pagination", paginationJson);
        
        return new ResponseEntity<>(productPage.getContent(), headers, HttpStatus.OK);
    }
    
    @Operation(summary = "Update a product", description = "Updates an existing product by ID")
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductResponse>> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated product object", required = true)
            @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productService.updateProduct(id, product)
                .thenApply(updatedProduct -> {
                    if (updatedProduct != null) {
                        return ResponseEntity.ok(updatedProduct);
                    }
                    return ResponseEntity.notFound().<ProductResponse>build();
                });
    }
    
    @Operation(summary = "Delete a product", description = "Soft deletes a product by ID")
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<ResponseModel>> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable UUID id) {
        return productService.deleteProduct(id)
                .thenApply(response -> ResponseEntity.ok(response));
    }
}
