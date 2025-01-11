package org.emangini.servolution.core.review.services;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.core.review.ReviewService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.core.review.persistence.ReviewEntity;
import org.emangini.servolution.core.review.persistence.ReviewRepository;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private static final String INVALID_PRODUCT_ID = "Invalid productId: ";

    private final Scheduler jdbcScheduler;
    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewServiceImpl(
            @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
            ReviewRepository repository,
            @Qualifier("reviewMapperImpl") ReviewMapper mapper,
            ServiceUtil serviceUtil
    ) {
        this.jdbcScheduler = jdbcScheduler;
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Observed(
            name = "createReview",
            contextualName = "review-service.create-review"
    )
    @Override
    public Mono<Review> createReview(Review body) {

        if (body.getProductId() < 1) {
            throw new InvalidInputException(INVALID_PRODUCT_ID + body.getProductId());
        }

        return Mono.fromCallable(() -> blockingCreateReview(body))
                .subscribeOn(jdbcScheduler);

    }

    @Observed(
            name = "getReviews",
            contextualName = "review-service.get-reviews"
    )
    @Override
    public Flux<Review> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException(INVALID_PRODUCT_ID + productId);
        }

        log.info("Calling getReviews for product with id={}", productId);

        return Mono.fromCallable(() -> blockingGetReviews(productId))
                .flatMapMany(Flux::fromIterable)
                .log(log.getName(), FINE)
                .subscribeOn(jdbcScheduler);
    }

    @Observed(
            name = "deleteReviews",
            contextualName = "review-service.delete-reviews"
    )
    @Override
    public Mono<Void> deleteReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException(INVALID_PRODUCT_ID + productId);
        }

        return Mono.fromRunnable(() -> blockingDeleteReviews(productId))
                .subscribeOn(jdbcScheduler)
                .then();
    }

    /*
        Helper Methods
     */
    @Observed(
            name = "blockingCreateReview",
            contextualName = "review-service.blocking-create-review"
    )
    private Review blockingCreateReview(Review body) {

        try {
            ReviewEntity entity = mapper.apiToEntity(body);
            ReviewEntity savedEntity = repository.save(entity);

            log.debug("createReview: created a review entity {}/{}", body.getProductId(), body.getReviewId());
            return mapper.entityToApi(savedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidInputException(
                    "Duplicate key, Product Id: " + body.getReviewId()
                    + ", Review Id: " + body.getReviewId()
            );
        }
    }

    @Observed(
            name = "blockingGetReviews",
            contextualName = "review-service.blocking-get-reviews"
    )
    private List<Review> blockingGetReviews(int productId) {

        List<ReviewEntity> entities = repository.findByProductId(productId);
        List<Review> reviews = mapper.entityListToApiList(entities);
        reviews.forEach(review -> review.setServiceAddress(serviceUtil.getServiceAddress()));

        log.debug("Response size: {}", reviews.size());

        return reviews;
    }

    @Observed(
            name = "blockingDeleteReviews",
            contextualName = "review-service.blocking-delete-reviews"
    )
    private void blockingDeleteReviews(int productId) {
        log.debug("deleteReviews: attempts to delete reviews for product w/ productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }

}
