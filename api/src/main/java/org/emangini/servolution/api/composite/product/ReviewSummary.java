package org.emangini.servolution.api.composite.product;

public record ReviewSummary(
        int reviewId,
        String author,
        String subject
) { }
