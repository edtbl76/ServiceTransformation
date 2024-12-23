package org.emangini.servolution.composite.product.services;

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

import java.util.List;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAggregate getProduct(int productId) {

        Product product = integration.getProduct(productId);
        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        // product info
        int productId = product.productId();
        String productName = product.name();
        int productWeight = product.weight();

        // summarize recommendations
        List<RecommendationSummary> recommendationSummaries =
                (recommendations == null) ? null :
                        recommendations.stream().map(recommendation -> new RecommendationSummary(
                                recommendation.recommendationId(),
                                recommendation.author(),
                                recommendation.rate()
                        )).toList();

        // summarize review3s
        List<ReviewSummary> reviewSummaries =
                (reviews == null) ? null :
                        reviews.stream().map(review -> new ReviewSummary(
                                review.reviewId(),
                                review.author(),
                                review.subject()
                        )).toList();

        // get service address and aggregate them
        String productAddress = product.serviceAddress();

        String recommendationAddress =
                (recommendations != null && !recommendations.isEmpty())
                        ? recommendations.get(0).serviceAddress() : "";

        String reviewAddress =
                (reviews != null && !reviews.isEmpty())
                        ? reviews.get(0).serviceAddress() : "";

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
