package org.emangini.servolution.core.product;

import org.emangini.servolution.core.product.persistence.ProductEntity;
import org.emangini.servolution.core.product.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.Sort.Direction.ASC;

@DataMongoTest
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private ProductRepository repository;

    private ProductEntity savedEntity;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        ProductEntity entity = new ProductEntity(1, "name", 1);
        savedEntity = repository.save(entity);

        assertEqualsProduct(entity, savedEntity);
    }

    @Test
    void create() {

        ProductEntity entity = new ProductEntity(2, "name2", 2);
        repository.save(entity);

        ProductEntity foundEntity = repository.findById(entity.getId()).get();
        assertEqualsProduct(entity, foundEntity);
        assertEquals(2, repository.count());
    }

    @Test
    void update() {
        savedEntity.setName("name2");
        repository.save(savedEntity);

        // Todo handle Optional
        ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals("name2", foundEntity.getName());
        assertEquals(1, foundEntity.getVersion());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getByProductId() {
        Optional<ProductEntity> foundEntity = repository.findByProductId(savedEntity.getProductId());

        assertTrue(foundEntity.isPresent());
        assertEqualsProduct(savedEntity, foundEntity.get());
    }

    @Test
    void validateDuplicateKeyException() {
        ProductEntity duplicateEntity = new ProductEntity(savedEntity.getProductId(), "name", 1);
        assertThrows(DuplicateKeyException.class, () -> repository.save(duplicateEntity));
    }

    @Test
    void validateOptimisticLockingFailureException() {

        // Todo handle Optional
        // Store saved setup entity into 2 separate entity objects
        ProductEntity entity1 = repository.findById(savedEntity.getId()).get();
        ProductEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update entity using the first entity object (which will increment the version number)
        entity1.setName("nameUpdatedFromEntity1");
        repository.save(entity1);

        // Update entity using second entity, which should fail because 2nd entity has the original version
        entity2.setName("nameUpdatedFromEntity2");
        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(entity2));
    }

    @Test
    void validatePagination() {

        repository.deleteAll();

        List<ProductEntity> products = rangeClosed(101,110)
                .mapToObj(id -> new ProductEntity(id, "name" + id, id))
                .toList();
        repository.saveAll(products);

        Pageable nextPage = PageRequest.of(0, 4, ASC, "productId");
        nextPage = testNextPage(nextPage, "[101, 102, 103, 104]", true);
        nextPage = testNextPage(nextPage, "[105, 106, 107, 108]", true);
        nextPage = testNextPage(nextPage, "[109, 110]", false);

    }

    /*
        Helper Methods
     */
    private Pageable testNextPage(Pageable page, String expectedProductIds, boolean expectsNextPage) {
        Page<ProductEntity> productPage = repository.findAll(page);

        String pageMap = productPage.getContent().stream()
                .map(ProductEntity::getProductId)
                .toList()
                .toString();

        assertEquals(expectedProductIds, pageMap);
        assertEquals(expectsNextPage, productPage.hasNext());
        return productPage.nextPageable();
    }

    private void assertEqualsProduct(ProductEntity expected, ProductEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWeight(), actual.getWeight());
    }
}
