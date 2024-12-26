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
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public void createProduct(ProductAggregate body) {

        try {
            log.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.productId());

            Product product = new Product(
                    body.productId(),
                    body.name(),
                    body.weight(),
                    null);
            integration.createProduct(product);

            if (body.recommendations() != null) {
                body.recommendations().forEach(recommendationSummary -> {
                    Recommendation recommendation = new Recommendation(
                            body.productId(),
                            recommendationSummary.recommendationId(),
                            recommendationSummary.author(),
                            recommendationSummary.rate(),
                            recommendationSummary.content(),
                            null
                    );
                    integration.createRecommendation(recommendation);
                });
            }

            if (body.reviews() != null) {
                body.reviews().forEach(reviewSummary -> {
                    Review review = new Review(
                            body.productId(),
                            reviewSummary.reviewId(),
                            reviewSummary.author(),
                            reviewSummary.subject(),
                            reviewSummary.content(),
                            null
                    );
                    integration.createReview(review);
                });
            }
        } catch (RuntimeException e) {
            log.warn("createCompositeProduct failed", e);
            throw e;
        }
    }

    @Override
    public ProductAggregate getProduct(int productId) {

        log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Product product = integration.getProduct(productId);
        if (product == null) {
            throw new NotFoundException("No product found for productId: " + productId);
        }

        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);
        log.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    @Override
    public void deleteProduct(int productId) {
        log.debug("deleteCompositeProduct: Deletes a product aggregate for productId: {}", productId);
        integration.deleteProduct(productId);
        integration.deleteRecommendations(productId);
        integration.deleteReviews(productId);

        log.debug("deleteCompositeProduct: aggregate entities deleted for productId: {}", productId);
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
