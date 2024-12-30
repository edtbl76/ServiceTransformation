package org.emangini.servolution.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ServiceAddresses {

    private final String compositeAddress;
    private final String productAddress;
    private final String recommendationAddress;
    private final String reviewAddress;

    public ServiceAddresses() {
        this(null, null, null, null);
    }

}