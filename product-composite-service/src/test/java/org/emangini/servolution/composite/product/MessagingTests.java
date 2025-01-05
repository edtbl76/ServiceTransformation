package org.emangini.servolution.composite.product;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.composite.product.ProductAggregate;
import org.emangini.servolution.api.composite.product.RecommendationSummary;
import org.emangini.servolution.api.composite.product.ReviewSummary;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.emangini.servolution.api.event.Event.Type.CREATE;
import static org.emangini.servolution.api.event.Event.Type.DELETE;
import static org.emangini.servolution.composite.product.IsSameEvent.sameEventExceptCreatedAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static reactor.core.publisher.Mono.just;


@Slf4j
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {TestSecurityConfiguration.class},
        properties = {
                "spring.security.oauth2.resourceserver.jwt.issuer-uri=",
                "spring.main.allow-bean-definition-overriding=true",
                "eureka.client.enabled=false"
        }
)
@Import({TestChannelBinderConfiguration.class})
class MessagingTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private OutputDestination outputDestination;

    @BeforeEach
    void setUp() {
        // purge topics
        purgeMessages("products");
        purgeMessages("recommendations");
        purgeMessages("reviews");
    }

    @Test
    void createPartialCompositeProduct() {

        // Create partial composite product message
        ProductAggregate compositeProduct = new ProductAggregate(
                1,
                "name",
                1,
                null,
                null,
                null
        );
        postAndVerifyProduct(compositeProduct, ACCEPTED);

        // read queues
        final List<String> productMessages = getMessages("products");
        final List<String> recommendationMessages = getMessages("recommendations");
        final List<String> reviewMessages = getMessages("reviews");


        // Confirm that a product message was created.
        assertEquals(1, productMessages.size());

        // Create an event from the message
        Event<Integer, Product> expectedEvent = new Event<>(
                CREATE,
                compositeProduct.getProductId(),
                new Product(
                        compositeProduct.getProductId(),
                        compositeProduct.getName(),
                        compositeProduct.getWeight(),
                        null));

        // Validate that the message represents the same event we just created.
        assertThat(productMessages.get(0), is(sameEventExceptCreatedAt(expectedEvent)));

        // Validate that no recommendations or reviews were created
        assertEquals(0, recommendationMessages.size());
        assertEquals(0, reviewMessages.size());
    }

    @Test
    void createFullCompositeProduct() {

        /*
            - Create full composite product object
            - post and verify that it was accepted
         */
        ProductAggregate compositeProduct = new ProductAggregate(
                1,
                "name",
                1,
                singletonList(
                        new RecommendationSummary(1, "author", 1, "content")),
                singletonList(
                        new ReviewSummary(1, "author", "subject", "content")),
                null
        );
        postAndVerifyProduct(compositeProduct, ACCEPTED);

        // Read queues
        final List<String> productMessages = getMessages("products");
        final List<String> recommendationMessages = getMessages("recommendations");
        final List<String> reviewMessages = getMessages("reviews");

        /*
            1. confirm that a product message was created
            2. create en event from the composite object above
            3. confirm that the queued message is the same as the Event
         */
        assertEquals(1, productMessages.size());
        Event<Integer, Product> expectedProductEvent = new Event<>(
                CREATE,
                compositeProduct.getProductId(),
                new Product(
                        compositeProduct.getProductId(),
                        compositeProduct.getName(),
                        compositeProduct.getWeight(),
                        null));
        assertThat(productMessages.get(0), is(sameEventExceptCreatedAt(expectedProductEvent)));


        /*
            1. confirm that a recommendation message was created
            2. create en event from the composite object above
            3. confirm that the queued message is the same as the Event
        */
        assertEquals(1, recommendationMessages.size());
        RecommendationSummary recommendationSummary = compositeProduct.getRecommendations().get(0);
        Event<Integer, Recommendation> expectedRecommendationEvent = new Event<>(
                CREATE,
                compositeProduct.getProductId(),
                new Recommendation(
                        compositeProduct.getProductId(),
                        recommendationSummary.getRecommendationId(),
                        recommendationSummary.getAuthor(),
                        recommendationSummary.getRate(),
                        recommendationSummary.getContent(),
                        null
                ));
        assertThat(recommendationMessages.get(0), is(sameEventExceptCreatedAt(expectedRecommendationEvent)));

        /*
            1. confirm that a review message was created
            2. create en event from the composite object above
            3. confirm that the queued message is the same as the Event
        */
        assertEquals(1, reviewMessages.size());
        ReviewSummary reviewSummary = compositeProduct.getReviews().get(0);
        Event<Integer, Review> expectedReviewEvent = new Event<>(
                CREATE,
                compositeProduct.getProductId(),
                new Review(
                        compositeProduct.getProductId(),
                        reviewSummary.getReviewId(),
                        reviewSummary.getAuthor(),
                        reviewSummary.getSubject(),
                        reviewSummary.getContent(),
                        null
                ));
        assertThat(reviewMessages.get(0), is(sameEventExceptCreatedAt(expectedReviewEvent)));
    }

    @Test
    void deleteCompositeProduct() {

        deleteAndVerifyProduct(1, ACCEPTED);

        final List<String> productMessages = getMessages("products");
        final List<String> recommendationMessages = getMessages("recommendations");
        final List<String> reviewMessages = getMessages("reviews");

        /*
            1. assert 1 delete product event is queue dup
            2. manually create product delete event
            3. validate that the two are the same.
         */
        assertEquals(1, productMessages.size());
        Event<Integer, Product> expectedProductEvent = new Event<>(DELETE, 1, null);
        assertThat(productMessages.get(0), is(sameEventExceptCreatedAt(expectedProductEvent)));

        /*
            1. assert 1 delete recommendation event is queue dup
            2. manually create recommendation delete event
            3. validate that the two are the same.
         */
        assertEquals(1, recommendationMessages.size());
        Event<Integer, Product> expectedRecommendationEvent = new Event<>(DELETE, 1, null);
        assertThat(recommendationMessages.get(0), is(sameEventExceptCreatedAt(expectedRecommendationEvent)));

        /*
            1. assert 1 delete review event is queue dup
            2. manually create review delete event
            3. validate that the two are the same.
        */
        assertEquals(1, reviewMessages.size());
        Event<Integer, Product> expectedReviewEvent = new Event<>(DELETE, 1, null);
        assertThat(reviewMessages.get(0), is(sameEventExceptCreatedAt(expectedReviewEvent)));
    }


    /*
        Helper Methods
     */
    private void purgeMessages(String bindingName) {
        getMessages(bindingName);
    }

    private List<String> getMessages(String bindingName) {
        List<String> messages = new ArrayList<>();
        boolean moreMessages = true;

        while (moreMessages) {
            Message<byte []> message = getMessage(bindingName);

            if (message == null) {
                moreMessages = false;
            } else {
                messages.add(new String(message.getPayload()));
            }
        }
        return messages;
    }

    private Message<byte []> getMessage(String bindingName) {
        try {
            return outputDestination.receive(0, bindingName);
        } catch (NullPointerException e) {
            log.error("getMessage() caught NullPointerException with binding = {}", bindingName);
            return null;
        }
    }

    // TODO handle inline values
    private void postAndVerifyProduct(ProductAggregate compositeProduct, HttpStatus expectedStatus) {
        client.post()
                .uri("/product-composite")
                .body(just(compositeProduct), ProductAggregate.class)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    // TODO handle inline values
    private void deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
        client.delete()
                .uri("/product-composite/" + productId)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}
