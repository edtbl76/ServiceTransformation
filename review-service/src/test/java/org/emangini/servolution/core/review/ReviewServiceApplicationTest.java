package org.emangini.servolution.core.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReviewServiceApplicationTest {

    @Autowired
    private WebTestClient client;

    @Test
    void contextLoads() { }

    @Test
    void getReviewsByProductId() {

        int productId = 1;

        client.get()
                .uri("/review?productId=" + productId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].productId").isEqualTo(productId);
    }

    @Test
    void getReviewsMissingParameter() {

        client.get()
                .uri("/review")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/review")
                .jsonPath("$.message")
                    .isEqualTo("Required query parameter 'productId' is not present.");
    }

    @Test
    void getReviewsInvalidParameter() {

        client.get()
                .uri("/review?productId=no-integer")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/review")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getReviewsNotFound() {

        int productIdNotFound = 213;

        client.get()
                .uri("/review?productId=" + productIdNotFound)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);

    }

    @Test
    void getReviewsInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

        client.get()
                .uri("/review?productId=" + productIdInvalid)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/review")
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }
}