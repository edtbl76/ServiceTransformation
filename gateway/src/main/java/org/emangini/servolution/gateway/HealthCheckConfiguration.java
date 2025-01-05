package org.emangini.servolution.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;


import static java.util.logging.Level.FINE;

@Slf4j
@Configuration
public class HealthCheckConfiguration {

    private final WebClient webClient;

    @Autowired
    public HealthCheckConfiguration(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Bean
    ReactiveHealthContributor servicesHealthCheck() {

        final Map<String, ReactiveHealthIndicator> healthCheckRegistry = new LinkedHashMap<>();

        healthCheckRegistry.put("product", () -> getHeath("http://product"));
        healthCheckRegistry.put("recommendation", () -> getHeath("http://recommendation"));
        healthCheckRegistry.put("review", () -> getHeath("http://review"));
        healthCheckRegistry.put("product-composite", () -> getHeath("http://product-composite"));

        return CompositeReactiveHealthContributor.fromMap(healthCheckRegistry);
    }


    private Mono<Health> getHeath(String baseUrl) {
        String healthUrl = baseUrl + "/actuator/health";
        log.debug("Calling healthCheck API at URL: {}", healthUrl);

        return webClient.get()
                .uri(healthUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(string -> new Health.Builder().up().build())
                .onErrorResume(throwable -> Mono.just(new Health.Builder().down(throwable).build()))
                .log(log.getName(), FINE);
    }


}
