package com.app.orderproductservice.service;

import com.app.orderproductservice.entity.Order;
import com.app.orderproductservice.entity.OrderStatus;
import java.util.UUID;

public interface OrderService {
    Order createOrder(UUID userId, UUID productId, Integer quantity);
    Order getOrder(UUID orderId);
    Order updateOrderStatus(UUID orderId, OrderStatus status);
} 