package org.emangini.servolution.core.recommendation.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.recommendation.RecommendationService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.emangini.servolution.core.recommendation.persistence.RecommendationRepository;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(
            RecommendationRepository repository,
            @Qualifier("recommendationMapperImpl") RecommendationMapper mapper,
            ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {

        try {
            RecommendationEntity entity = mapper.apiToEntity(body);
            RecommendationEntity savedEntity = repository.save(entity);

            log.debug("createRecommendation: entity created with productId/recommendationId: {}/{}",
                    body.getProductId(), body.getRecommendationId());
            return mapper.entityToApi(savedEntity);
        } catch (DuplicateKeyException e) {
            throw new InvalidInputException(
                    "Duplicate key, Product Id: " + body.getProductId()
                            + ", Recommendation Id: " + body.getRecommendationId()
            );
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        List<RecommendationEntity> entities = repository.findByProductId(productId);
        List<Recommendation> recommendations = mapper.entityListToApiList(entities);
        recommendations.forEach(recommendation ->
                recommendation.setServiceAddress(serviceUtil.getServiceAddress()));

        log.debug("getRecommendations: response size: {}", recommendations.size());
        return recommendations;
    }

    @Override
    public void deleteRecommendations(int productId) {
        log.debug("deleteRecommendations: attempts to delete an entity with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }
}
