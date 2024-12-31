package org.emangini.servolution.core.review.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.core.review.ReviewService;
import org.emangini.servolution.api.event.Event;
import org.emangini.servolution.api.exceptions.EventProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class MessageProcessorConfig {

    private final ReviewService reviewService;

    @Autowired
    public MessageProcessorConfig(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Bean
    public Consumer<Event<Integer, Review>> messageProcessor() {
        return integerReviewEvent -> {
            log.info("Process message created at {}...", integerReviewEvent.getEventCreatedAt());

            switch (integerReviewEvent.getEventType()) {
                case CREATE -> {
                    Review review = integerReviewEvent.getData();
                    log.info("Creating review with id: {}/{}", review.getProductId(), review.getReviewId());
                    reviewService.createReview(review).block();
                }

                case DELETE -> {
                    int productId = integerReviewEvent.getKey();
                    log.info("Deleting review with id: {}", productId);
                    reviewService.deleteReviews(productId).block();
                }

                default -> {
                    String errorMessage = "Incorrect event type: " + integerReviewEvent.getEventType()
                            + ", expected a CREATE or DELETE event";
                    log.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
                }
            }
            log.info("Message processing complete");
        };
    }
}
