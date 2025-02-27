package org.emangini.servolution.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReviewSummary {

    private final int reviewId;
    private final String author;
    private final String subject;
    private final String content;

    public ReviewSummary() {
        this( 0, null, null, null);
    }
}