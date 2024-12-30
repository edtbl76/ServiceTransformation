package org.emangini.servolution.core.recommendation;

import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.emangini.servolution.core.recommendation.services.RecommendationMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTests {

    private final RecommendationMapper mapper = Mappers.getMapper(RecommendationMapper.class);

    @Test
    void mapperTests() {
        assertNotNull(mapper);

        Recommendation api = new Recommendation(
                1,
                2,
                "author",
                4,
                "content",
                "serviceAddress"
        );
        RecommendationEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getRecommendationId(), entity.getRecommendationId());
        assertEquals(api.getAuthor(), entity.getAuthor());
        assertEquals(api.getRate(), entity.getRating());
        assertEquals(api.getContent(), entity.getContent());

        Recommendation apiMappedFromEntity = mapper.entityToApi(entity);

        assertEquals(api.getProductId(), apiMappedFromEntity.getProductId());
        assertEquals(api.getRecommendationId(), apiMappedFromEntity.getRecommendationId());
        assertEquals(api.getAuthor(), apiMappedFromEntity.getAuthor());
        assertEquals(api.getRate(), apiMappedFromEntity.getRate());
        assertEquals(api.getContent(), apiMappedFromEntity.getContent());
        assertNull(apiMappedFromEntity.getServiceAddress());
    }

    @Test
    void mapperListTests() {

        assertNotNull(mapper);

        Recommendation api = new Recommendation(
                1,
                2,
                "author",
                4,
                "content",
                "serviceAddress"
        );
        List<Recommendation> apiList1 = singletonList(api);

        List<RecommendationEntity> entities = mapper.apiListToEntityList(apiList1);
        assertEquals(apiList1.size(), entities.size());

        RecommendationEntity entity = entities.get(0);
        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getRecommendationId(), entity.getRecommendationId());
        assertEquals(api.getAuthor(), entity.getAuthor());
        assertEquals(api.getRate(), entity.getRating());
        assertEquals(api.getContent(), entity.getContent());

        List<Recommendation> apiList2 = mapper.entityListToApiList(entities);
        assertEquals(apiList1.size(), apiList2.size());

        Recommendation api2 = apiList2.get(0);
        assertEquals(api.getProductId(), api2.getProductId());
        assertEquals(api.getRecommendationId(), api2.getRecommendationId());
        assertEquals(api.getAuthor(), api2.getAuthor());
        assertEquals(api.getRate(), api2.getRate());
        assertEquals(api.getContent(), api2.getContent());
        assertNull(api2.getServiceAddress());
    }
}
