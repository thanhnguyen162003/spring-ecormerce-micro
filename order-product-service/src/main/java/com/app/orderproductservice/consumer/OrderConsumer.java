package com.app.orderproductservice.consumer;

import com.app.orderproductservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    @KafkaListener(
        topics = "${spring.kafka.template.default-topic}",
        groupId = "order-consumer-group"
    )
    public void consume(OrderPlacedEvent event) {
        log.info("Received order event: OrderId={}, UserId={}, ProductId={}, Quantity={}, TotalPrice={}, Status={}, CreatedAt={}",
            event.getOrderId(),
            event.getUserId(),
            event.getProductId(),
            event.getQuantity(),
            event.getTotalPrice(),
            event.getStatus(),
            event.getCreatedAt()
        );
    }
} 