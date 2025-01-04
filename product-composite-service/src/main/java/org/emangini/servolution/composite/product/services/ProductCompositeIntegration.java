package org.emangini.servolution.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.product.ProductService;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.recommendation.RecommendationService;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.core.review.ReviewService;
import org.emangini.servolution.api.event.Event;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.emangini.servolution.util.http.HttpErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.IOException;

import static java.util.logging.Level.FINE;
import static org.emangini.servolution.api.event.Event.Type.CREATE;
import static org.emangini.servolution.api.event.Event.Type.DELETE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static reactor.core.publisher.Flux.empty;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    //TODO TLS
    private final static String PRODUCT_SERVICE_URL = "http://product";
    private final static String RECOMMENDATION_SERVICE_URL = "http://recommendation";
    private final static String REVIEW_SERVICE_URL = "http://review";


    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final Scheduler publishEventScheduler;
    private final StreamBridge streamBridge;



    @Autowired
    public ProductCompositeIntegration(
            /*
                Set this to @Lazy to avoid circular dependency w/ product composite service application
             */
            @Qualifier("publishEventScheduler") @Lazy Scheduler publishEventScheduler,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            StreamBridge streamBridge) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.streamBridge = streamBridge;
    }


    @PostConstruct
    public void init() {
        log.info("Validating injected beans...");
        if (publishEventScheduler == null) {
            throw new IllegalStateException("publishEventScheduler is not properly initialized!");
        }
        if (webClient == null) {
            throw new IllegalStateException("WebClient is not properly initialized!");
        }
        if (streamBridge == null) {
            throw new IllegalStateException("StreamBridge is not properly initialized!");
        }
        log.info("All beans validated successfully.");
    }

    @Override
    public Mono<Product> createProduct(Product body) {

        return Mono.fromCallable(() -> {
            sendMessage("products-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);

    }

    @Override
    public Mono<Product> getProduct(int productId) {

        String url = PRODUCT_SERVICE_URL + "/product/" + productId;
        log.debug("Calling getProduct API on URL: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Product.class)
                .log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {

        return Mono.fromRunnable(() -> sendMessage("products-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();

    }

    @Override
    public Mono<Recommendation> createRecommendation(Recommendation body) {

        return Mono.fromCallable(() -> {
            sendMessage("recommendations-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {

        String url = RECOMMENDATION_SERVICE_URL + "/recommendation?productId=" + productId;
        log.debug("Calling getRecommendations API on URL: {}", url);

        /*
            Returns an empty result so composite supports partial results if something happens during the
            call to the recommendation service.
         */
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Recommendation.class)
                .log(log.getName(), FINE)
                .onErrorResume(error -> empty());

    }

    @Override
    public Mono<Void> deleteRecommendations(int productId) {

        return Mono.fromRunnable(() -> sendMessage("recommendations-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }

    @Override
    public Mono<Review> createReview(Review body) {

        return Mono.fromCallable(() -> {
            sendMessage("reviews-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Flux<Review> getReviews(int productId) {

        String url = REVIEW_SERVICE_URL + "/review?productId=" + productId;
        log.debug("Calling getReviews API on URL: {}", url);

         /*
            Returns an empty result so composite supports partial results if something happens during the
            call to the review service.
         */
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .log(log.getName(), FINE)
                .onErrorResume(error -> empty());

    }

    @Override
    public Mono<Void> deleteReviews(int productId) {

        return Mono.fromRunnable(() -> sendMessage("reviews-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }

    public Mono<Health> getProductHealth() {
        return getHealth(PRODUCT_SERVICE_URL);
    }
    public Mono<Health> getRecommendationHealth() {
        return getHealth(RECOMMENDATION_SERVICE_URL);
    }
    public Mono<Health> getReviewHealth() {
        return getHealth(REVIEW_SERVICE_URL);
    }
    // Helpers
    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        log.debug("Calling HealthCheck API at URL: {}", url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(healthResult -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(log.getName(), FINE);
    }
    /*
        Helper Methods
     */

    // TODO handle raw use of Event, Message
    private void sendMessage(String bindingName, Event event) {
        log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                        .setHeader("partitionKey", event.getKey())
                                .build();

        streamBridge.send(bindingName, message);
    }

    private Throwable handleException(Throwable throwable) {

        if (!(throwable instanceof WebClientResponseException e)) {
            log.warn("Unexpected error: {}, rethrowing", throwable.toString());
            return throwable;
        }

        HttpStatusCode statusCode = e.getStatusCode();
        if (statusCode.equals(NOT_FOUND)) {
            throw new NotFoundException(getErrorMessage(e));
        } else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
            throw new InvalidInputException(getErrorMessage(e));
        }
        log.warn("Unexpected error: {}, rethrowing.", e.getResponseBodyAsString());
        log.warn("Error body: {}", e.getResponseBodyAsString());
        return e;

    }

    private String getErrorMessage(WebClientResponseException exception) {
        try {
            return objectMapper
                    .readValue(
                            exception.getResponseBodyAsString(),
                            HttpErrorInfo.class)
                    .getMessage();
        } catch (IOException e) {
            return exception.getMessage();
        }
    }
}
