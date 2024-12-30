package org.emangini.servolution.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.product.ProductService;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.recommendation.RecommendationService;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.core.review.ReviewService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.emangini.servolution.util.http.HttpErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(RestTemplate restTemplate,
                                       ObjectMapper objectMapper,

                                       @Value("${app.product-service.host}")
                                       String productServiceHost,
                                       @Value("${app.product-service.port}")
                                       String productServicePort,

                                       @Value("${app.recommendation-service.host}")
                                       String recommendationServiceHost,
                                       @Value("${app.recommendation-service.port}")
                                       String recommendationServicePort,

                                       @Value("${app.review-service.host}")
                                       String reviewServiceHost,
                                       @Value("${app.review-service.port}")
                                       String reviewServicePort

    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

        this.productServiceUrl = String.format("http://%s:%s/product",
                productServiceHost,
                productServicePort);

        this.recommendationServiceUrl = String.format("http://%s:%s/recommendation",
                recommendationServiceHost,
                recommendationServicePort);

        this.reviewServiceUrl = String.format("http://%s:%s/review",
                reviewServiceHost,
                reviewServicePort);
    }

    @Override
    public Product createProduct(Product body) {
        try {
            String url = this.productServiceUrl;
            log.debug("Calling createProduct API on URL: {}", url);

            Product product = restTemplate.postForObject(url, body, Product.class);

            // Todo handle null
            log.debug("Created product with id: {}", product.getProductId());

            return product;

        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    @Override
    public Product getProduct(int productId) {

        try {
            String url = this.productServiceUrl + "/" + productId;
            log.debug("Calling getProduct API on URL: {}", url);

            Product product = restTemplate.getForObject(url, Product.class);

            // TODO handle null
            log.debug("Found product with id: {}", product.getProductId());

            return product;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            String url = this.productServiceUrl + "/" + productId;
            log.debug("Calling deleteProduct API on URL: {}", url);
            restTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {

        try {
            String url = this.recommendationServiceUrl;
            log.debug("Calling createRecommendation API on URL: {}", url);

            Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
            // TODO handle null
            log.debug("Created recommendation with product id: {}", recommendation.getProductId());

            return recommendation;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        try {
            String url = this.recommendationServiceUrl + "?productId=" + productId;
            log.debug("Calling getRecommendations API on URL: {}", url);
            List<Recommendation> recommendations = restTemplate
                    .exchange(
                            url,
                            GET,
                            null,
                            new ParameterizedTypeReference<List<Recommendation>>() {})
                    .getBody();

            // TODO handle null
            log.debug("Found {} recommendations for product width with id: {}",
                    recommendations.size(),
                    productId);

            return recommendations;
        } catch (RuntimeException e) {
            log.warn("Got an exception while requesting recommendations, return zero recommendations: {}",
                    e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteRecommendations(int productId) {
        try {
            String url = this.recommendationServiceUrl + "?productId=" + productId;
            log.debug("Calling deleteRecommendations API on URL: {}", url);
            restTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    @Override
    public Review createReview(Review body) {
        try {
            String url = this.reviewServiceUrl;
            log.debug("Calling createReview API at URL: {}", url);
            Review review = restTemplate.postForObject(url, body, Review.class);
            log.warn("Created a review with product id: {}", review.getProductId());
            return review;
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }
    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = this.reviewServiceUrl + "?productId=" + productId;
            log.debug("Calling getReviews API on URL: {}", url);
            List<Review> reviews = restTemplate
                    .exchange(
                            url,
                            GET,
                            null,
                            new ParameterizedTypeReference<List<Review>>() {})
                    .getBody();

            // TODO handle null
            log.debug("Found {} reviews for product with id: {}", reviews.size(), productId);
            return reviews;
        } catch (RuntimeException e) {
            log.warn("Got an exception while requesting reviews, return zero reviews: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteReviews(int productId) {
        try {
            String url = this.reviewServiceUrl + "?productId=" + productId;
            log.debug("Calling deleteReviews API on URL: {}", url);
            restTemplate.delete(url);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientErrorException(e);
        }
    }

    /*
        Helper Methods
     */
    private RuntimeException handleHttpClientErrorException(HttpClientErrorException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        if (statusCode.equals(NOT_FOUND)) {
            throw new NotFoundException(getErrorMessage(exception));
        } else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
            throw new InvalidInputException(getErrorMessage(exception));
        } else {
            log.warn("Got an unexpected HTTP error: {}, rethrowing.", exception.getStatusCode());
            log.warn("Error Body: {}", exception.getResponseBodyAsString());
            throw exception;
        }
    }

    private String getErrorMessage(HttpClientErrorException exception) {
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
