package org.emangini.servolution.core.review;

import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.core.review.persistence.ReviewEntity;
import org.emangini.servolution.core.review.services.ReviewMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTests {

    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        Review api = new Review(
                1,
                2,
                "author",
                "subject",
                "content",
                "serviceAddress"
        );


        ReviewEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getReviewId(), entity.getReviewId());
        assertEquals(api.getAuthor(), entity.getAuthor());
        assertEquals(api.getSubject(), entity.getSubject());
        assertEquals(api.getContent(), entity.getContent());

        Review apiMappedFromEntity = mapper.entityToApi(entity);

        assertEquals(api.getProductId(), apiMappedFromEntity.getProductId());
        assertEquals(api.getReviewId(), apiMappedFromEntity.getReviewId());
        assertEquals(api.getAuthor(), apiMappedFromEntity.getAuthor());
        assertEquals(api.getSubject(), apiMappedFromEntity.getSubject());
        assertEquals(api.getContent(), apiMappedFromEntity.getContent());
        assertNull(apiMappedFromEntity.getServiceAddress());
    }

    @Test
    void mapperListTests() {

        assertNotNull(mapper);

        Review api = new Review(
                1,
                2,
                "author",
                "subject",
                "content",
                "serviceAddress"
        );

        List<Review> reviews = singletonList(api);

        List<ReviewEntity> entities = mapper.apiListToEntityList(reviews);
        assertEquals(reviews.size(), entities.size());

        ReviewEntity entity = entities.get(0);

        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getReviewId(), entity.getReviewId());
        assertEquals(api.getAuthor(), entity.getAuthor());
        assertEquals(api.getSubject(), entity.getSubject());
        assertEquals(api.getContent(), entity.getContent());

        List<Review> reviewFromEntities = mapper.entityListToApiList(entities);
        assertEquals(reviews.size(), reviewFromEntities.size());

        Review apiFromMapping = reviewFromEntities.get(0);

        assertEquals(api.getProductId(), apiFromMapping.getProductId());
        assertEquals(api.getReviewId(), apiFromMapping.getReviewId());
        assertEquals(api.getAuthor(), apiFromMapping.getAuthor());
        assertEquals(api.getSubject(), apiFromMapping.getSubject());
        assertEquals(api.getContent(), apiFromMapping.getContent());
        assertNull(apiFromMapping.getServiceAddress());
    }


}
