package org.emangini.servolution.core.recommendation;

import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.emangini.servolution.core.recommendation.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

@DataMongoTest
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private RecommendationRepository repository;

    private RecommendationEntity savedEntity;

    @BeforeEach
    void setupDb() {

        repository.deleteAll();

        RecommendationEntity entity = new RecommendationEntity(
                1,
                2,
                "author",
                3,
                "content"
        );

        savedEntity = repository.save(entity);
        assertEqualsRecommendation(entity, savedEntity);
    }
    @Test
    void create() {

        RecommendationEntity entity = new RecommendationEntity(
                1,
                3,
                "author",
                3,
                "content"
        );

        repository.save(entity);

        // TODO handle Optional
        RecommendationEntity fetchedEntity = repository.findById(entity.getId()).get();
        assertEqualsRecommendation(entity, fetchedEntity);

        assertEquals(2, repository.count());
    }
    @Test
    void update() {
        savedEntity.setAuthor("author2");
        repository.save(savedEntity);

        // TODO handle Optional

        RecommendationEntity fetchedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long) fetchedEntity.getVersion());
        assertEquals("author2", fetchedEntity.getAuthor());
    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getByProductId() {
        List<RecommendationEntity> entities = repository.findByProductId(savedEntity.getProductId());
        assertThat(entities, hasSize(1));
        assertEqualsRecommendation(savedEntity, entities.get(0));
    }

    @Test
    void validateDuplicateKeyException() {
        RecommendationEntity entity = new RecommendationEntity(
                1,
                2,
                "author",
                3,
                "content"
        );
        assertThrows(DuplicateKeyException.class, () -> repository.save(entity));
    }
    @Test
    void validateOptimisticLockingFailureException() {

        // Todo handle Optional
        // Store saved setup entity into 2 separate entity objects
        RecommendationEntity entityOne = repository.findById(savedEntity.getId()).get();
        RecommendationEntity entityTwo = repository.findById(savedEntity.getId()).get();

        // Update entity using the first entity object (which will increment the version number)
        entityOne.setAuthor("authorUpdatedFromEntityOne");
        repository.save(entityOne);

        // update entity using second entity, which fails because 2nd entity has original version.
        entityTwo.setAuthor("authorUpdatedFromEntityTwo");

        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(entityTwo));
    }

    /*
        Helper Methods
     */
    private void assertEqualsRecommendation(RecommendationEntity expected, RecommendationEntity actual) {

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getRecommendationId(), actual.getRecommendationId());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getRating(), actual.getRating());
        assertEquals(expected.getContent(), actual.getContent());
    }

}
