package org.emangini.servolution.composite.product.services;

import io.micrometer.observation.annotation.Observed;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.FINE;
import static org.springframework.security.core.context.ReactiveSecurityContextHolder.getContext;

@RestController
@Slf4j
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private static final String NO_JWT_TESTING_MSG = "No JWT based Authentication supplied. Are we running tests??";
    private final SecurityContext nullSecurityContext = new SecurityContextImpl();

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }


    @Observed(
            name = "createProduct",
            contextualName = "product-composite-service.create-product"
    )
    @Override
    public Mono<Void> createProduct(ProductAggregate body) {

        try {
            // TODO handle raw use of Mono
            List<Mono> monos = new ArrayList<>();

            monos.add(getLogAuthorizationInfoMono());

            log.debug("createCompositeProduct::{}::CREATING", body.getProductId());

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

            log.debug("createCompositeProduct::productId::{}::CREATED", body.getProductId());

            // TODO handle duplicate error messages
            return Mono.zip(objects -> "", monos.toArray(new Mono[0]))
                    .doOnError(throwable ->
                            log.warn("createCompositeProduct::FAILED::{}", throwable.toString()))
                    .then();
        } catch (RuntimeException e) {
            log.warn("createCompositeProduct::FAILED::{}", e.toString());
            throw e;
        }
    }

    @Observed(
            name = "getProduct",
            contextualName = "product-composite-service.get-product"
    )
    @Override
    public Mono<ProductAggregate> getProduct(int productId, int delay, int faultPercent) {

        log.debug("getCompositeProduct::productId::{}", productId);

        // TODO handle unchecked casts
        return Mono.zip(objects -> createProductAggregate(
                                (SecurityContext) objects[0],
                                (Product) objects[1],
                                (List<Recommendation>) objects[2],
                                (List<Review>) objects[3],
                                serviceUtil.getServiceAddress()),
                        getSecurityContextMono(),
                        integration.getProduct(productId, delay, faultPercent),
                        integration.getRecommendations(productId).collectList(),
                        integration.getReviews(productId).collectList())
                .doOnError(throwable -> log.warn("getCompositeProduct::FAILED::{}", throwable.toString()))
                .log(log.getName(), FINE);

    }

    @Observed(
            name = "deleteProduct",
            contextualName = "product-composite-service.delete-product"
    )
    @Override
    public Mono<Void> deleteProduct(int productId) {

        try {
            log.debug("deleteCompositeProduct::productId::{}", productId);

            // TODO: calling zip on void object has no effect
            return Mono.zip(objects -> "",
                            getLogAuthorizationInfoMono(),
                            integration.deleteProduct(productId),
                            integration.deleteRecommendations(productId),
                            integration.deleteReviews(productId))
                    .doOnError(throwable -> log.warn("deleteCompositeProduct::FAILED::{}", throwable.toString()))
                    .log(log.getName(), FINE)
                    .then();

        } catch (RuntimeException e) {
            log.warn("deleteCompositeProduct::FAILED::{}", e.toString());
            throw e;
        }

    }

    @Observed(
            name = "createProductAggregate",
            contextualName = "product-composite-service.create-product-aggregate"
    )
    private ProductAggregate createProductAggregate(
            SecurityContext securityContext,
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        logAuthorizationInfo(securityContext);

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

    private Mono<SecurityContext> getLogAuthorizationInfoMono() {

        return getSecurityContextMono().doOnNext(this::logAuthorizationInfo);
    }

    private Mono<SecurityContext> getSecurityContextMono() {
        return getContext().defaultIfEmpty(nullSecurityContext);
    }

    private void logAuthorizationInfo(SecurityContext securityContext) {

        if (securityContext != null
                && securityContext.getAuthentication() != null
                && securityContext.getAuthentication() instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            Jwt token = jwtAuthenticationToken.getToken();
            logAuthorizationInfo(token);
        } else {
            log.warn(NO_JWT_TESTING_MSG);
        }
    }

    private void logAuthorizationInfo(Jwt jwt) {

        if (jwt == null) {
            log.warn(NO_JWT_TESTING_MSG);
        } else {
            if (log.isInfoEnabled()) {
                URL issuer = jwt.getIssuer();
                List<String> audience = jwt.getAudience();
                Object subject = jwt.getSubject();
                Object scopes = jwt.getClaims().get("scope");
                Object expires = jwt.getExpiresAt();

                log.debug("AuthorizationInfo: Subject: {}, scopes: {}, expires: {}, issuer: {}, audience: {}",
                        subject, scopes, expires, issuer, audience == null ? "null" : audience.toString());


                //                  set for debugging.
                log.debug("JWT Headers: {}", jwt.getHeaders());
                log.debug("JWT Claims: {}", jwt.getClaims());
            }
        }
    }
}
