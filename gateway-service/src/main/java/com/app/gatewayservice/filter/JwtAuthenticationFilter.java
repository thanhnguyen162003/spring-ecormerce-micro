//package com.app.gatewayservice.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import javax.crypto.SecretKey;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
//
//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    public JwtAuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
//            }
//
//            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//            if (!authHeader.startsWith("Bearer ")) {
//                return onError(exchange, "No Bearer token", HttpStatus.UNAUTHORIZED);
//            }
//
//            String jwt = authHeader.substring(7);
//            try {
//                Claims claims = Jwts.parser()
//                        .verifyWith((SecretKey) getSigningKey())
//                        .build()
//                        .parseSignedClaims(token)
//                        .getPayload();
//
//                // Convert numeric role to role name
//                List<String> roles = new ArrayList<>();
//                Integer roleId = claims.get("role", Integer.class);
//                if (roleId != null) {
//                    switch (roleId) {
//                        case 1:
//                            roles.add("ADMIN");
//                            break;
//                        case 2:
//                            roles.add("USER");
//                            break;
//                    }
//                }
//
//                // Add user roles to request headers for downstream services
//                if (!roles.isEmpty()) {
//                    exchange.getRequest().mutate()
//                            .header("X-User-Roles", String.join(",", roles))
//                            .build();
//                }
//
//                // Add user ID to request headers
//                String userId = claims.getSubject();
//                exchange.getRequest().mutate()
//                        .header("X-User-ID", userId)
//                        .build();
//
//                // Forward the original JWT token to downstream services
//                exchange.getRequest().mutate()
//                        .header("X-JWT-Token", jwt)
//                        .build();
//
//            } catch (Exception e) {
//                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
//            }
//
//            return chain.filter(exchange);
//        };
//    }
//
//    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
//        exchange.getResponse().setStatusCode(httpStatus);
//        return exchange.getResponse().setComplete();
//    }
//
//    public static class Config {
//    }
//}