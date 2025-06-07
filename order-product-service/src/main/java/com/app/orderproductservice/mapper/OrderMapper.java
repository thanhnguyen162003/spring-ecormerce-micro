package com.app.orderproductservice.mapper;

import com.app.orderproductservice.dto.OrderRequest;
import com.app.orderproductservice.dto.OrderResponse;
import com.app.orderproductservice.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    
    public Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        return order;
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
} 