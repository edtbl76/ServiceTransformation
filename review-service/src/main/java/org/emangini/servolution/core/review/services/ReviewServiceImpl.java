package org.emangini.servolution.core.review.services;

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

    @Override
    public Mono<Review> createReview(Review body) {

        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }

        return Mono.fromCallable(() -> blockingCreateReview(body))
                .subscribeOn(jdbcScheduler);

    }


    @Override
    public Flux<Review> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        log.info("Calling getReviews for product with id={}", productId);

        return Mono.fromCallable(() -> blockingGetReviews(productId))
                .flatMapMany(Flux::fromIterable)
                .log(log.getName(), FINE)
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        return Mono.fromRunnable(() -> blockingDeleteReviews(productId))
                .subscribeOn(jdbcScheduler)
                .then();
    }

    /*
        Helper Methods
     */

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

    private List<Review> blockingGetReviews(int productId) {

        List<ReviewEntity> entities = repository.findByProductId(productId);
        List<Review> reviews = mapper.entityListToApiList(entities);
        reviews.forEach(review -> review.setServiceAddress(serviceUtil.getServiceAddress()));

        log.debug("Response size: {}", reviews.size());

        return reviews;
    }


    private void blockingDeleteReviews(int productId) {
        log.debug("deleteReviews: attempts to delete reviews for product w/ productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }

}
