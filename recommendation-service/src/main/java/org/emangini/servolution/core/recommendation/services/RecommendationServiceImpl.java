package org.emangini.servolution.core.recommendation.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.recommendation.Recommendation;
import org.emangini.servolution.api.core.recommendation.RecommendationService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if (productId == 13) {
            log.debug("No recommendations found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Recommendation> recommendations = new ArrayList<>();

        recommendations.add(new Recommendation(
                productId,
                1,
                "Author 1",
                1,
                "Content 1",
                serviceUtil.getServiceAddress()
        ));

        recommendations.add(new Recommendation(
                productId,
                2,
                "Author 2",
                2,
                "Content 2",
                serviceUtil.getServiceAddress()
        ));

        recommendations.add(new Recommendation(
                productId,
                3,
                "Author 3",
                3,
                "Content 3",
                serviceUtil.getServiceAddress()
        ));

        log.debug("/recommendation response size: {}", recommendations.size());
        return recommendations;
    }
}
