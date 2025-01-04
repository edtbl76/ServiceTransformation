package org.emangini.servolution.core.recommendation;

import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.event.Event;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.core.recommendation.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

import static org.emangini.servolution.api.event.Event.Type.CREATE;
import static org.emangini.servolution.api.event.Event.Type.DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {"eureka.client.enabled=false"}
)
class RecommendationServiceApplicationTests extends MongoDbTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private RecommendationRepository repository;

    @Autowired
    @Qualifier("messageProcessor")
    private Consumer<Event<Integer, Recommendation>> messageProcessor;

    @BeforeEach
    void setupDb() {
        repository.deleteAll().block();
    }

    @Test
    void contextLoads() { }

    @Test
    void getRecommendationsByProductId() {

        int productId = 1;

        sendCreateRecommendationEvent(productId, 1);
        sendCreateRecommendationEvent(productId, 2);
        sendCreateRecommendationEvent(productId, 3);

        assertEquals(3, repository.findByProductId(productId).count().block());

        getAndVerifyRecommendationByProductId(productId, OK)
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[2].productId").isEqualTo(productId)
                .jsonPath("$[2].recommendationId").isEqualTo(3);
    }

    @Test
    void deleteRecommendations() {

        int productId = 1;
        int recommendationId = 1;

        // SETUP: create recommendation event validate that repo increments
        sendCreateRecommendationEvent(productId, recommendationId);
        assertEquals(1, repository.findByProductId(productId).count().block());

        // test delete and validate the recommendation is deleted
        sendDeleteRecommendationEvent(productId);
        assertEquals(0, repository.findByProductId(productId).count().block());

        // Idempotency check
        sendDeleteRecommendationEvent(productId);

    }

    @Disabled
    @Test
    void validateDuplicationFailure() {

        int productId = 1;
        int recommendationId = 1;

        sendCreateRecommendationEvent(productId, recommendationId);

        assertEquals(1, repository.count().block());

        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> sendCreateRecommendationEvent(productId, recommendationId),
                "Expected InvalidInputException");

        assertEquals("Duplicate key, Product Id: " + productId
                + ", Recommendation Id: " + recommendationId, thrown.getMessage());
        assertEquals(1, repository.count().block());
    }

    @Test
    void getRecommendationMissingParameter() {

        getAndVerifyRecommendationByProductId("", BAD_REQUEST)
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message")
                    .isEqualTo("Required query parameter 'productId' is not present.");
    }

    @Test
    void getRecommendationsInvalidParameter() {

        getAndVerifyRecommendationByProductId("?productId=no-integer", BAD_REQUEST)
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getRecommendationsNotFound() {

        int productIdNotFound = 389;

        // TODO fix status for NOT FOUND
        getAndVerifyRecommendationByProductId(productIdNotFound, OK)
                .jsonPath("$.length()").isEqualTo(0);

    }

    @Test
    void getRecommendationsInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

        getAndVerifyRecommendationByProductId(productIdInvalid, UNPROCESSABLE_ENTITY)
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }

    /*
        Helper methods
     */
    private void sendCreateRecommendationEvent(int productId, int recommendationId) {
        Recommendation recommendation = new Recommendation(
                productId,
                recommendationId,
                "Author " + recommendationId,
                recommendationId,
                "Content" + recommendationId,
                "serviceAddress"
        );
        Event<Integer, Recommendation> event = new Event<>(CREATE, productId, recommendation);
        messageProcessor.accept(event);
    }

    private void sendDeleteRecommendationEvent(int productId) {
        Event<Integer, Recommendation> event = new Event<>(DELETE, productId, null);
        messageProcessor.accept(event);
    }

    private WebTestClient.BodyContentSpec getAndVerifyRecommendationByProductId(
            int productId,
            HttpStatus expectedStatus)
    {
        return getAndVerifyRecommendationByProductId("?productId=" + productId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyRecommendationByProductId(
            String productIdQuery,
            HttpStatus expectedStatus)
    {
        return client.get()
                .uri("/recommendation" + productIdQuery)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

}