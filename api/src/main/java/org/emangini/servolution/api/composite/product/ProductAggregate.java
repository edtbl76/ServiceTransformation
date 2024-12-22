package org.emangini.servolution.api.composite.product;

import java.util.List;

public record ProductAggregate(
        String name,
        int weight,
        List<RecommendationSummary> recommendations,
        List<ReviewSummary> reviews,
        ServiceAddresses serviceAddresses
) { }
