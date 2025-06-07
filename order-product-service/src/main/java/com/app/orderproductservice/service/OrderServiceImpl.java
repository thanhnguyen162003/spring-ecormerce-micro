package com.app.orderproductservice.service;

import com.app.orderproductservice.dto.ProductResponse;
import com.app.orderproductservice.entity.Order;
import com.app.orderproductservice.entity.OrderStatus;
import com.app.orderproductservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.product.url}")
    private String productServiceUrl;

    @Value("${service.user.url}")
    private String userServiceUrl;

    @Override
    @Transactional
    public Order createOrder(UUID userId, UUID productId, Integer quantity) {
        // Validate user
        Boolean isUserActive = restTemplate.getForObject(
            userServiceUrl + "/api/users/" + userId + "/status",
            Boolean.class
        );
        if (isUserActive == null || !isUserActive) {
            throw new RuntimeException("User is not active");
        }

        // Validate product and get price
        ProductResponse product = restTemplate.getForObject(
            productServiceUrl + "/api/products/" + productId,
            ProductResponse.class
        );
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient product quantity");
        }

        // Calculate total price
        BigDecimal totalPrice = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity));

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
} 