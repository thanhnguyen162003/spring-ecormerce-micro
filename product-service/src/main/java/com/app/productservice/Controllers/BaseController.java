package com.app.productservice.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Base Controller", description = "Base controller with common configurations")
public abstract class BaseController {
    
    protected static final String SUCCESS_MESSAGE = "Operation completed successfully";
    protected static final String ERROR_MESSAGE = "An error occurred during the operation";
    
    @Operation(summary = "Health check endpoint", description = "Returns the health status of the service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy"),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is healthy");
    }
} 