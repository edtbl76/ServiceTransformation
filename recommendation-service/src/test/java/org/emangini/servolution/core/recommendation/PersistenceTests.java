package org.emangini.servolution.core.recommendation;

import io.micrometer.observation.ObservationRegistry;
import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.emangini.servolution.core.recommendation.persistence.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

@DataMongoTest(properties = {"spring.cloud.config.enabled=false"})
public class PersistenceTests extends MongoDbTestBase {

    // Only required a mock of the registry to pass this test.
    @MockitoBean
    private ObservationRegistry observationRegistry;

    @Autowired
    private RecommendationRepository repository;

    private RecommendationEntity savedEntity;

    @BeforeEach
    void setupDb() {

        repository.deleteAll().block();

        RecommendationEntity entity = new RecommendationEntity(
                1,
                2,
                "author",
                3,
                "content"
        );
        savedEntity = repository.save(entity).block();

        // TODO handle nullable
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
        repository.save(entity).block();

        // TODO handle nullable
        RecommendationEntity fetchedEntity = repository.findById(entity.getId()).block();
        assertEqualsRecommendation(entity, fetchedEntity);

        assertEquals(2, repository.count().block());
    }

    @Test
    void update() {
        savedEntity.setAuthor("author2");
        repository.save(savedEntity).block();

        // TODO handle nullable
        RecommendationEntity fetchedEntity = repository.findById(savedEntity.getId()).block();
        assertEquals(1, (long) fetchedEntity.getVersion());
        assertEquals("author2", fetchedEntity.getAuthor());
    }

    @Test
    void delete() {
        repository.delete(savedEntity).block();
        assertNotEquals(TRUE, repository.existsById(savedEntity.getId()).block());
    }

    @Test
    void getByProductId() {
        List<RecommendationEntity> entities = repository
                .findByProductId(savedEntity.getProductId())
                .collectList()
                .block();

        assertThat(entities, hasSize(1));

        // TODO handle nullable
        assertEqualsRecommendation(savedEntity, entities.get(0));
    }

    @Disabled
    @Test
    void validateDuplicateKeyException() {
        RecommendationEntity entity = new RecommendationEntity(
                1,
                2,
                "author",
                3,
                "content"
        );
        assertThrows(DuplicateKeyException.class, () -> repository.save(entity).block());
    }
    @Test
    void validateOptimisticLockingFailureException() {

        // Store saved setup entity into 2 separate entity objects
        RecommendationEntity entityOne = repository.findById(savedEntity.getId()).block();
        RecommendationEntity entityTwo = repository.findById(savedEntity.getId()).block();

        // TODO handle nullable
        // Update entity using the first entity object (which will increment the version number)
        entityOne.setAuthor("authorUpdatedFromEntityOne");
        repository.save(entityOne).block();

        // TODO handle nullable
        // update entity using second entity, which fails because 2nd entity has original version.
        entityTwo.setAuthor("authorUpdatedFromEntityTwo");

        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(entityTwo).block());
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
