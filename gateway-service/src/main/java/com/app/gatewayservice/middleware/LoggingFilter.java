package com.app.gatewayservice.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = UUID.randomUUID().toString();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Log request
        logger.info("Request ID: {} | Method: {} | Path: {} | Headers: {}",
                requestId,
                request.getMethod(),
                request.getURI().getPath(),
                request.getHeaders());

        // Log request body if present
        if (request.getHeaders().getContentLength() > 0) {
            return DataBufferUtils.join(request.getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        logger.info("Request ID: {} | Request Body: {}", requestId, body);
                        
                        // Create new request with the same body
                        ServerHttpRequest newRequest = request.mutate()
                                .header("X-Request-ID", requestId)
                                .build();
                        
                        // Continue with the new request
                        return chain.filter(exchange.mutate().request(newRequest).build())
                                .then(Mono.fromRunnable(() -> {
                                    // Log response
                                    logger.info("Request ID: {} | Response Status: {} | Headers: {}",
                                            requestId,
                                            response.getStatusCode(),
                                            response.getHeaders());
                                }));
                    });
        }

        // If no request body, just log the response
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    logger.info("Request ID: {} | Response Status: {} | Headers: {}",
                            requestId,
                            response.getStatusCode(),
                            response.getHeaders());
                }));
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
} 