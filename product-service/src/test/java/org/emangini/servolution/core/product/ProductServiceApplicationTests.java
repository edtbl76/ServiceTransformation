package org.emangini.servolution.core.product;

import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.event.Event;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.core.product.persistence.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.*;


@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {"eureka.client.enabled=false"}
)
class ProductServiceApplicationTests extends MongoDbTestBase {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ProductRepository repository;

    @Autowired
    @Qualifier("messageProcessor")
    private Consumer<Event<Integer, Product>> messageProcessor;

    @BeforeEach
    void setupDb() {
        repository.deleteAll().block();
    }

    @Test
    void contextLoads() { }

    @Test
    void getProductById() {

        int productId = 1;

        // Validate that product doesn't exist
        assertNull(repository.findByProductId(productId).block());
        assertEquals(0, repository.count().block());

        // send create event
        sendCreateProductEvent(productId);

        // validate product exists
        assertNotNull(repository.findByProductId(productId).block());
        assertEquals(1, repository.count().block());

        // validate that the product is correct
        getAndVerifyProduct(productId, OK)
                .jsonPath("$.productId").isEqualTo(productId)
                .jsonPath("$.name").isEqualTo("Name " + productId);

    }

    @Test
    void deleteProduct() {

        int productId = 1;

        // SETUP: create product, validate
        sendCreateProductEvent(productId);
        assertNotNull(repository.findByProductId(productId).block());

        // delete the product
        sendDeleteProductEvent(productId);

        // validate that product is gone
        assertNull(repository.findByProductId(productId).block());

        // Idempotency Check
        sendDeleteProductEvent(productId);

    }

    @Disabled
    @Test
    void validateDuplicationFailure() {

        int productId = 1;

        /*
            SETUP:
            - validate product doesn't exist
            - send create event
            - validate that a product is created.
         */
        assertNull(repository.findByProductId(productId).block());
        sendCreateProductEvent(productId);
        assertNotNull(repository.findByProductId(productId).block());

        // send a 2nd create; validate duplicate exception
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> sendCreateProductEvent(productId), "Expected InvalidInputException");
        assertEquals("Duplicate key, Product Id: " + productId, thrown.getMessage());

    }

    @Test
    void getProductInvalidParameterString() {

        getAndVerifyProduct("/no-integer", BAD_REQUEST)
                .jsonPath("$.path").isEqualTo("/product/no-integer")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getProductNotFound() {

        int productIdNotFound = 13;

        getAndVerifyProduct(productIdNotFound, NOT_FOUND)
                .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
                .jsonPath("$.message")
                    .isEqualTo("No product found for productId: " + productIdNotFound);
    }

    @Test
    void getProductInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

        getAndVerifyProduct(productIdInvalid, UNPROCESSABLE_ENTITY)
                .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }

    /*
        Helper methods
     */
    private void sendCreateProductEvent(int productId) {

        Product product = new Product(
                productId,
                "Name " + productId,
                productId,
                "serviceAddress"
        );

        Event<Integer, Product> event = new Event<>(CREATE, productId, product);
        messageProcessor.accept(event);
    }

    private void sendDeleteProductEvent(int productId) {

        Event<Integer, Product> event = new Event<>(DELETE, productId, null);
        messageProcessor.accept(event);
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId,HttpStatus expectedStatus) {
        return getAndVerifyProduct("/" + productId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(String productIdQuery, HttpStatus expectedStatus) {
        return client.get()
                .uri("/product" + productIdQuery)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

}