package org.emangini.servolution.core.recommendation.services;

import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mapping(target = "rate", source = "rating")
    @Mapping(target = "serviceAddress", ignore = true)
    Recommendation entityToApi(RecommendationEntity entity);

    @Mapping(target = "rating", source = "rate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    RecommendationEntity apiToEntity(Recommendation api);

    List<Recommendation> entityListToApiList(List<RecommendationEntity> entity);
    List<RecommendationEntity> apiListToEntityList(List<Recommendation> ap);


}
