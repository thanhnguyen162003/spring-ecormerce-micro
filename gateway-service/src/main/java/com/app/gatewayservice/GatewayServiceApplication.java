package com.app.gatewayservice;

import com.app.gatewayservice.filter.JwtAuthenticationFilter;
import com.app.gatewayservice.filter.RoleAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, 
										 JwtAuthenticationFilter jwtAuthFilter,
										 RoleAuthorizationFilter roleAuthFilter) {
		return builder.routes()
				// Public routes (no auth required)
				.route("user_service_public", r -> r
						.path("/api/users/login", "/api/users/register")
						.and()
						.method(HttpMethod.POST)
						.uri("lb://USER-SERVICE"))
				
				// Product Service Routes
				.route("product_service", r -> r
						.path("/api/products/**")
						.filters(f -> f
								.stripPrefix(1)  // Remove /api prefix
								.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config()))
								.filter(roleAuthFilter.apply(config -> {
									config.setRequiredRoles(List.of("ADMIN", "USER"));
								})))
						.uri("lb://PRODUCT-SERVICE"))
				
				.route("user_service", r -> r
						.path("/api/users/**")
						.uri("lb://USER-SERVICE"))
				
				// Order Service Routes
				.route("order_service", r -> r
						.path("/api/orders/**")
						.filters(f -> f
								.stripPrefix(1)  // Remove /api prefix
								.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config()))
								.filter(roleAuthFilter.apply(config -> {
									config.setRequiredRoles(List.of("ADMIN", "USER"));
								})))
						.uri("lb://ORDER-SERVICE"))
				.build();
	}
}
