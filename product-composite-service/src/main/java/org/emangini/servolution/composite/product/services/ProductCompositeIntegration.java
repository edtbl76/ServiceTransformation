package org.emangini.servolution.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.observation.annotation.Observed;
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
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.IOException;
import java.net.URI;

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

    private final ServiceUtil serviceUtil;


    @Autowired
    public ProductCompositeIntegration(
            /*
                Set this to @Lazy to avoid circular dependency w/ product composite service application
             */
            @Qualifier("publishEventScheduler") @Lazy Scheduler publishEventScheduler,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            StreamBridge streamBridge,
            ServiceUtil serviceUtil) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.streamBridge = streamBridge;
        this.serviceUtil = serviceUtil;
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

    @Observed(
            name = "createProduct",
            contextualName = "product-composite-integration.create-product"
    )
    @Override
    public Mono<Product> createProduct(Product body) {

        return Mono.fromCallable(() -> {
            sendMessage("products-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);

    }

    @Observed(
            name = "getProduct",
            contextualName = "product-composite-integration.get-product"
    )
    @Override
    @Retry(name = "product")
    @TimeLimiter(name = "product")
    @CircuitBreaker(name = "product", fallbackMethod = "getProductFallbackValue")
    public Mono<Product> getProduct(int productId, int delay, int faultPercent) {

        URI url = UriComponentsBuilder
                .fromUriString(PRODUCT_SERVICE_URL + "/product/{productId}?delay={delay}&faultPercent={faultPercent}")
                .build(productId, delay, faultPercent);

        log.info("Calling getProduct API on URL: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Product.class)
                .log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    @Observed(
            name = "deleteProduct",
            contextualName = "product-composite-integration.delete-product"
    )
    @Override
    public Mono<Void> deleteProduct(int productId) {

        return Mono.fromRunnable(() -> sendMessage("products-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();

    }

    @Observed(
            name = "createRecommendation",
            contextualName = "product-composite-integration.create-recommendation"
    )
    @Override
    public Mono<Recommendation> createRecommendation(Recommendation body) {

        return Mono.fromCallable(() -> {
            sendMessage("recommendations-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Observed(
            name = "getRecommendations",
            contextualName = "product-composite-integration.get-recommendations"
    )
    @Override
    public Flux<Recommendation> getRecommendations(int productId) {

        URI url = UriComponentsBuilder
                .fromUriString(RECOMMENDATION_SERVICE_URL + "/recommendation?productId={productId}")
                .build(productId);

        log.info("Calling getRecommendations API on URL: {}", url);

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

    @Observed(
            name = "deleteRecommendations",
            contextualName = "product-composite-integration.delete-recommendations"
    )
    @Override
    public Mono<Void> deleteRecommendations(int productId) {

        return Mono.fromRunnable(() -> sendMessage("recommendations-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }

    @Observed(
            name = "createReview",
            contextualName = "product-composite-integration.create-review"
    )
    @Override
    public Mono<Review> createReview(Review body) {

        return Mono.fromCallable(() -> {
            sendMessage("reviews-out-0", new Event<>(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @Observed(
            name = "getReviews",
            contextualName = "product-composite-integration.get-reviews"
    )
    @Override
    public Flux<Review> getReviews(int productId) {

        URI url = UriComponentsBuilder
                .fromUriString(REVIEW_SERVICE_URL + "/review?productId={productId}")
                .build(productId);

        log.info("Calling getReviews API on URL: {}", url);

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

    @Observed(
            name = "deleteReviews",
            contextualName = "product-composite-integration.delete-reviews"
    )
    @Override
    public Mono<Void> deleteReviews(int productId) {

        return Mono.fromRunnable(() -> sendMessage("reviews-out-0",
                        new Event<>(DELETE, productId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }


    /*
        Helper Methods
     */
    private Mono<Product> getProductFallbackValue(
            int productId,
            int delay,
            int faultPercent,
            CallNotPermittedException exception) {

        log.warn("Creating a fail-fast product for productId = {}, delay = {}, faultPercent = {}, and exception = {}",
                productId, delay, faultPercent, exception.toString());

        if (productId == 13) {
            String errorMessage = "Product Id: " + productId + " not found in fallback cache.";
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return Mono.just(new Product(
                productId,
                "Fallback product" + productId,
                productId,
                serviceUtil.getServiceAddress()
        ));

    }

    // TODO handle raw use of Event, Message
    @Observed(
            name = "sendMessage",
            contextualName = "product-composite-integration.send-message"
    )
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
