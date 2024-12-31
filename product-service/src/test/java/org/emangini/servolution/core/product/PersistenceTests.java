package org.emangini.servolution.core.product;

import org.emangini.servolution.core.product.persistence.ProductEntity;
import org.emangini.servolution.core.product.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import reactor.test.StepVerifier;

import java.util.Objects;

@DataMongoTest
class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private ProductRepository repository;

    private ProductEntity savedEntity;

    @BeforeEach
    void setupDb() {
        /*
            StepVerifiers are a way to test reactive code by walking through the seps one at a time in a 2-phase
            approach.
            - Do the thing
            - Wait for a result
         */

        StepVerifier.create(repository.deleteAll()).verifyComplete();

        ProductEntity entity = new ProductEntity(1, "name", 1);

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(productEntity -> {
                   savedEntity = productEntity;
                   return areProductsEqual(entity, savedEntity);
                }).verifyComplete();
    }

    @Test
    void create() {

        ProductEntity entity = new ProductEntity(2, "name2", 2);

        // Test Create
        StepVerifier.create(repository.save(entity))
                .expectNextMatches(productEntity ->
                        areProductsEqual(entity, productEntity))
                .verifyComplete();

        // Validate that it is the correct object
        StepVerifier.create(repository.findById(entity.getId()))
                .expectNextMatches(productEntity ->
                        areProductsEqual(entity, productEntity))
                .verifyComplete();

        // Validate that the repository has the correct number of objects
        StepVerifier.create(repository.count())
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void update() {
        savedEntity.setName("name2");

        // Validate that updated entry exists
        StepVerifier.create(repository.save(savedEntity))
                .expectNextMatches(productEntity ->
                        areProductsEqual(savedEntity, productEntity))
                .verifyComplete();

        // Validate that the version has been updated and the name is correct
        StepVerifier.create(repository.findById(savedEntity.getId()))
                .expectNextMatches(productEntity ->
                        productEntity.getVersion() == 1 &&
                        productEntity.getName().equals("name2"))
                .verifyComplete();
    }

    @Test
    void delete() {

        // Validate that delete completes
        StepVerifier.create(repository.delete(savedEntity))
                .verifyComplete();

        // Validate that the object doesn't exist
        StepVerifier.create(repository.existsById(savedEntity.getId()))
                .expectNext(false)
                .verifyComplete();

    }

    @Test
    void getByProductId() {

        // Validate that Product w/ ID exists
        StepVerifier.create(repository.findByProductId(savedEntity.getProductId()))
                .expectNextMatches(productEntity ->
                        areProductsEqual(savedEntity, productEntity))
                .verifyComplete();


    }

    @Disabled
    @Test
    void validateDuplicateKeyException() {
        ProductEntity duplicateEntity = new ProductEntity(savedEntity.getProductId(), "name", 1);

        // Validate duplicate record throws DuplicateKeyException
        StepVerifier.create(repository.save(duplicateEntity))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void validateOptimisticLockingFailureException() {

        // Store saved setup entity into 2 separate entity objects
        ProductEntity entity1 = repository.findById(savedEntity.getId()).block();
        ProductEntity entity2 = repository.findById(savedEntity.getId()).block();

        // Todo handle nullable
        // Update entity using the first entity object (which will increment the version number)
        entity1.setName("nameUpdatedFromEntity1");
        repository.save(entity1).block();

        // Update entity using second entity, which should fail because 2nd entity has the original version
        entity2.setName("nameUpdatedFromEntity2");

        // Validates that trying to update second entry throws Error
        StepVerifier.create(repository.save(entity2))
                .expectError(OptimisticLockingFailureException.class)
                .verify();

        // Retrieve updated entity and validate that version number has been incremented and the name is updated
        StepVerifier.create(repository.findById(savedEntity.getId()))
                .expectNextMatches(productEntity ->
                        productEntity.getVersion() == 1 &&
                        productEntity.getName().equals("nameUpdatedFromEntity1"))
                .verifyComplete();

    }


    /*
        Helper methods
     */
    private boolean areProductsEqual(ProductEntity entity1, ProductEntity entity2) {
        return areFieldsEqual(entity1.getId(), entity2.getId()) &&
                areFieldsEqual(entity1.getVersion(), entity2.getVersion()) &&
                areFieldsEqual(entity1.getProductId(), entity2.getProductId()) &&
                areFieldsEqual(entity1.getName(), entity2.getName()) &&
                areFieldsEqual(entity1.getWeight(), entity2.getWeight());
    }

    private boolean areFieldsEqual(Object field1, Object field2) {
        return Objects.equals(field1, field2);
    }

}
