package com.app.orderproductservice.grpc;

import com.app.orderproductservice.entity.Order;
import com.app.orderproductservice.entity.OrderStatus;
import com.app.orderproductservice.proto.*;
import com.app.orderproductservice.service.OrderService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@GrpcService
@Service
@RequiredArgsConstructor
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderService orderService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            Order order = orderService.createOrder(
                UUID.fromString(request.getUserId()),
                UUID.fromString(request.getProductId()),
                request.getQuantity()
            );
            
            OrderResponse response = mapOrderToResponse(order);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getOrder(GetOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            Order order = orderService.getOrder(UUID.fromString(request.getOrderId()));
            OrderResponse response = mapOrderToResponse(order);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            Order order = orderService.updateOrderStatus(
                UUID.fromString(request.getOrderId()),
                OrderStatus.valueOf(request.getStatus())
            );
            
            OrderResponse response = mapOrderToResponse(order);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private OrderResponse mapOrderToResponse(Order order) {
        return OrderResponse.newBuilder()
            .setId(order.getId().toString())
            .setUserId(order.getUserId().toString())
            .setProductId(order.getProductId().toString())
            .setQuantity(order.getQuantity())
            .setTotalPrice(order.getTotalPrice().doubleValue())
            .setStatus(order.getStatus().name())
            .setCreatedAt(order.getCreatedAt().format(formatter))
            .setUpdatedAt(order.getUpdatedAt() != null ? order.getUpdatedAt().format(formatter) : "")
            .build();
    }
} 