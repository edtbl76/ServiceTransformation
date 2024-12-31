package org.emangini.servolution.composite.product.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.composite.product.ProductAggregate;
import org.emangini.servolution.api.composite.product.ProductCompositeService;
import org.emangini.servolution.api.composite.product.RecommendationSummary;
import org.emangini.servolution.api.composite.product.ReviewSummary;
import org.emangini.servolution.api.composite.product.ServiceAddresses;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
@Slf4j
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }


    @Override
    public Mono<Void> createProduct(ProductAggregate body) {

        try {
            // TODO handle raw use of Mono
            List<Mono> monos = new ArrayList<>();
            log.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.getProductId());

            Product product = new Product(
                    body.getProductId(),
                    body.getName(),
                    body.getWeight(),
                    null);
            monos.add(integration.createProduct(product));

            if (body.getRecommendations() != null) {
                body.getRecommendations().forEach(recommendationSummary -> {
                    Recommendation recommendation = new Recommendation(
                            body.getProductId(),
                            recommendationSummary.getRecommendationId(),
                            recommendationSummary.getAuthor(),
                            recommendationSummary.getRate(),
                            recommendationSummary.getContent(),
                            null
                    );
                    monos.add(integration.createRecommendation(recommendation));
                });
            }

            if (body.getReviews() != null) {
                body.getReviews().forEach(reviewSummary -> {
                    Review review = new Review(
                            body.getProductId(),
                            reviewSummary.getReviewId(),
                            reviewSummary.getAuthor(),
                            reviewSummary.getSubject(),
                            reviewSummary.getContent(),
                            null
                    );
                    monos.add(integration.createReview(review));
                });
            }

            log.debug("createCompositeProduct: composite entities created for productId: {}", body.getProductId());

            // TODO handle duplicate error messages
            return Mono.zip(objects -> "", monos.toArray(new Mono[0]))
                    .doOnError(throwable ->
                            log.warn("createCompositeProduct failed: {}", throwable.toString()))
                    .then();
        } catch (RuntimeException e) {
            log.warn("createCompositeProduct failed: {}", e.toString());
            throw e;
        }
    }

    @Override
    public Mono<ProductAggregate> getProduct(int productId) {

        log.info("calling getCompositeProduct for product with id: {}", productId);

        // TODO handle unchecked casts
        return Mono.zip(objects -> createProductAggregate(
                                (Product) objects[0],
                                (List<Recommendation>) objects[1],
                                (List<Review>) objects[2],
                                serviceUtil.getServiceAddress()),
                        integration.getProduct(productId),
                        integration.getRecommendations(productId).collectList(),
                        integration.getReviews(productId).collectList())
                .doOnError(throwable -> log.warn("getCompositeProduct failed: {}", throwable.toString()))
                .log(log.getName(), FINE);

    }

    @Override
    public Mono<Void> deleteProduct(int productId) {

        try {
            log.debug("deleteCompositeProduct: Deletes a product composite for productId: {}", productId);

            // TODO: calling zip on void object has no effect
            return Mono.zip(objects -> "",
                            integration.deleteProduct(productId),
                            integration.deleteRecommendations(productId),
                            integration.deleteReviews(productId))
                    .doOnError(throwable -> log.warn("delete failed: {}", throwable.toString()))
                    .log(log.getName(), FINE)
                    .then();

        } catch (RuntimeException e) {
            log.warn("deleteCompositeProduct failed: {}", e.toString());
            throw e;
        }

    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        // product info
        int productId = product.getProductId();
        String productName = product.getName();
        int productWeight = product.getWeight();

        // summarize recommendations
        List<RecommendationSummary> recommendationSummaries =
                (recommendations == null) ? null :
                        recommendations.stream().map(recommendation -> new RecommendationSummary(
                                recommendation.getRecommendationId(),
                                recommendation.getAuthor(),
                                recommendation.getRate(),
                                recommendation.getContent()
                        )).toList();

        // summarize reviews
        List<ReviewSummary> reviewSummaries =
                (reviews == null) ? null :
                        reviews.stream().map(review -> new ReviewSummary(
                                review.getReviewId(),
                                review.getAuthor(),
                                review.getSubject(),
                                review.getContent()
                        )).toList();

        // get service address and aggregate them
        String productAddress = product.getServiceAddress();

        String recommendationAddress =
                (recommendations != null && !recommendations.isEmpty())
                        ? recommendations.get(0).getServiceAddress() : "";

        String reviewAddress =
                (reviews != null && !reviews.isEmpty())
                        ? reviews.get(0).getServiceAddress() : "";

        ServiceAddresses serviceAddresses = new ServiceAddresses(
                serviceAddress, productAddress, reviewAddress, recommendationAddress
        );

        return new ProductAggregate(
                productId,
                productName,
                productWeight,
                recommendationSummaries,
                reviewSummaries,
                serviceAddresses
        );
    }

}
