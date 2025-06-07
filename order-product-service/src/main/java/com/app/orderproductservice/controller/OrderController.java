package com.app.orderproductservice.controller;

import com.app.orderproductservice.dto.OrderRequest;
import com.app.orderproductservice.dto.OrderResponse;
import com.app.orderproductservice.entity.Order;
import com.app.orderproductservice.entity.OrderStatus;
import com.app.orderproductservice.mapper.OrderMapper;
import com.app.orderproductservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(
            request.getUserId(),
            request.getProductId(),
            request.getQuantity()
        );
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
        @PathVariable UUID orderId,
        @RequestParam OrderStatus status
    ) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }
} 