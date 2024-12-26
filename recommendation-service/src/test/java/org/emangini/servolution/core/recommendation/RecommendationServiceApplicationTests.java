package org.emangini.servolution.core.recommendation;

import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.core.recommendation.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RecommendationServiceApplicationTests extends MongoDbTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private RecommendationRepository repository;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();
    }

    @Test
    void contextLoads() { }

    @Test
    void getRecommendationsByProductId() {

        int productId = 1;

        postAndVerifyRecommendationsByProductId(productId, 1, OK);
        postAndVerifyRecommendationsByProductId(productId, 2, OK);
        postAndVerifyRecommendationsByProductId(productId, 3, OK);

        assertEquals(3, repository.findByProductId(productId).size());

        getAndVerifyRecommendationByProductId(productId, OK)
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[2].productId").isEqualTo(productId)
                .jsonPath("$[2].recommendationId").isEqualTo(3);
    }

    @Test
    void deleteRecommendations() {

        int productId = 1;
        int recommendationId = 1;

        postAndVerifyRecommendationsByProductId(productId, recommendationId, OK)
                .jsonPath("$.productId").isEqualTo(productId)
                .jsonPath("$.recommendationId").isEqualTo(recommendationId);
        assertEquals(1, repository.findByProductId(productId).size());

        deleteAndVerifyRecommendationsByProductId(productId, OK);
        assertEquals(0, repository.findByProductId(productId).size());

        // Idempotency check
        deleteAndVerifyRecommendationsByProductId(productId, OK);

    }

    @Test
    void validateDuplicationFailure() {

        int productId = 1;
        int recommendationId = 1;

        postAndVerifyRecommendationsByProductId(productId, recommendationId, OK)
                .jsonPath("$.productId").isEqualTo(productId)
                .jsonPath("$.recommendationId").isEqualTo(recommendationId);

        assertEquals(1, repository.count());

        postAndVerifyRecommendationsByProductId(productId, recommendationId, UNPROCESSABLE_ENTITY)
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message")
                    .isEqualTo("Duplicate key, Product Id: "
                            + productId + ", Recommendation Id: " + recommendationId);

        assertEquals(1, repository.count());
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

    private WebTestClient.BodyContentSpec postAndVerifyRecommendationsByProductId(
            int productId,
            int recommendationId,
            HttpStatus expectedStatus
    ) {

        Recommendation recommendation = new Recommendation(
                productId,
                recommendationId,
                "Author " + recommendationId,
                recommendationId,
                "Content" + recommendationId,
                "serviceAddress"
        );

        return client.post()
                .uri("/recommendation")
                .body(just(recommendation), Recommendation.class)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private void deleteAndVerifyRecommendationsByProductId(
            int productId,
            HttpStatus expectedStatus)
    {
        client.delete()
                .uri("/recommendation?productId=" + productId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody();
    }
}