package org.emangini.servolution.core.review;

import org.emangini.servolution.core.review.persistence.ReviewEntity;
import org.emangini.servolution.core.review.persistence.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

@DataJpaTest(
        properties = {
                "spring.jpa.hibernate.ddl-auto=update",
                "spring.cloud.config.enabled=false"
        })
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = NONE)
public class PersistenceTests extends MySqlTestBase {

    @Autowired
    ReviewRepository repository;

    private ReviewEntity savedEntity;

    @BeforeEach
    void setupDb() {
        repository.deleteAll();

        ReviewEntity reviewEntity = new ReviewEntity(
                1, 2, "author", "subject", "content"
        );

        savedEntity = repository.save(reviewEntity);

        assertEqualsReview(reviewEntity, savedEntity);
    }

    @Test
    void create() {

        ReviewEntity createdEntity = new ReviewEntity(
                1, 3, "author", "subject", "content"
        );

        repository.save(createdEntity);

        // TODO: This is a test, so I don't know that I care about the optional, but handling it is good form?
        ReviewEntity foundEntity = repository.findById(createdEntity.getId()).get();
        assertEqualsReview(createdEntity, foundEntity);
        assertEquals(2, repository.count());
    }

    @Test
    void update() {

        savedEntity.setAuthor("author2");
        repository.save(savedEntity);

        // TODO: This is a test, so I don't know that I care about the optional, but handling it is good form?
        ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals("author2", foundEntity.getAuthor());
        assertEquals(1, repository.count());
        assertEquals(1, foundEntity.getVersion());

    }

    @Test
    void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    void getByProductId() {

        List<ReviewEntity> entities = repository.findByProductId(savedEntity.getProductId());

        assertThat(entities, hasSize(1));
        assertEqualsReview(savedEntity, entities.get(0));
    }

    @Test
    void validateDuplicationFailure() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            ReviewEntity duplicateEntity = new ReviewEntity(
                    1, 2, "author", "subject", "content"
            );
            repository.save(duplicateEntity);
        });
    }

    @Test
    void validateOptimisticLockingFailure() {

        // store same entity in 2 separate objects
        // TODO: This is a test, so I don't know that I care about the optional, but handling it is good form?
        ReviewEntity entity1 = repository.findById(savedEntity.getId()).get();
        ReviewEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update entity via the first instance
        entity1.setAuthor("author3");
        repository.save(entity1);

        /*
            Ensure we aren't able to update the entity w/ the second instance, because it holds the old version number.

            (NOTE: This pattern is important. It is the action of persisting the entity that will cause this exception
            as opposed to updating the entity.)
         */
        entity2.setAuthor("author4");
        assertThrows(OptimisticLockingFailureException.class, () -> repository.save(entity2));
    }

    private void assertEqualsReview(ReviewEntity expected, ReviewEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.getProductId(), actual.getProductId());
        assertEquals(expected.getReviewId(), actual.getReviewId());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getSubject(), actual.getSubject());
        assertEquals(expected.getContent(), actual.getContent());
    }
}
