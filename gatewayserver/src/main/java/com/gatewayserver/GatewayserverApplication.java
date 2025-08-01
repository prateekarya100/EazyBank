package com.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator routingConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder
				.routes()
				.route(p->p
						.path("/eazybank/accounts/**")//?<segment>.*) = '/${segment}'
						.filters(f->f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")))
						.uri("lb://ACCOUNTS"))

				.route(p->p
						.path("/eazybank/cards/**")
						.filters(f->f.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						)
						.uri("lb://CARDS"))

				.route(p->p
						.path("/eazybank/loans/**")
						.filters(f->f.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						)
						.uri("lb://LOANS")).build();

	}
}
