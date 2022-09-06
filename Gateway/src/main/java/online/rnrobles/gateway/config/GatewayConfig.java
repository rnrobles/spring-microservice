package online.rnrobles.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class GatewayConfig {

	private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

	@Bean
	@Profile("localhostRouter-noEureka")
	public RouteLocator configLocalNoEureka(RouteLocatorBuilder builder) {

		log.info("test");
		return builder.routes()
				.route("blue",
						r -> r.path("/blue/**").filters(f -> f.rewritePath("/blue/(?<segment>.*)", "/${segment}"))
								.uri("http://localhost:8701"))
				.route("red", r -> r.path("/red/**").filters(f -> f.rewritePath("/red/(?<segment>.*)", "/${segment}"))
						.uri("http://localhost:8700"))
				.build();
	}

	@Bean
	@Profile("localhost-eureka")
	public RouteLocator configLocalEureka(RouteLocatorBuilder builder) {
		log.info("test");

		return builder.routes()
				.route("blue",
						r -> r.path("/blue/**").filters(f -> f.rewritePath("/blue/(?<segment>.*)", "/${segment}"))
								.uri("lb://blue-service"))
				.route("red", r -> r.path("/red/**").filters(f -> f.rewritePath("/red/(?<segment>.*)", "/${segment}"))
						.uri("lb://red-service"))
				.build();
	}

	@Bean
	@Profile("localhost-eureka-cb")
	public RouteLocator configLocalEurekaCb(RouteLocatorBuilder builder) {
		return builder.routes().route("blue",
				r -> r.path("/blue/**").filters(f -> redirectCircuitBreaker(f, "/blue", "/green")).uri("lb://blue-service"))
				.route("red",
						r -> r.path("/red/**").filters(f -> f.rewritePath("/red/(?<segment>.*)", "/${segment}"))
								.uri("lb://red-service"))
				.route("green", r -> r.path("/green/**")
						.filters(f -> f.rewritePath("/green/(?<segment>.*)", "/${segment}")).uri("lb://green-service"))
				.build();
	}

	public GatewayFilterSpec redirectCircuitBreaker(GatewayFilterSpec f, String urlRewrite, String urlForward) {
		f.rewritePath( urlRewrite + "/(?<segment>.*)", "/${segment}");
		f.circuitBreaker(c -> {
			c.setName("failoverCB" + urlForward);
			f.filter((exchange, chain) -> {
				ServerHttpRequest req = exchange.getRequest();
				String path = req.getURI().getRawPath();
				log.info(path);
				c.setFallbackUri("forward:" + urlForward + path);
				return chain.filter(exchange.mutate().request(req).build());
			});
		});
		return f;
	}

}
