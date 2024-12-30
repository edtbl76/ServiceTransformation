package org.emangini.servolution.api.core.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Product {

    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;

    public Product() {
        this(0, null, 0, null);
    }

}
