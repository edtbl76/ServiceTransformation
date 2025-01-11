package org.emangini.servolution.api.core.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface ProductService {

    /**
     * Sample usage:
     *      curl -X POST ${HOST}:${PORT}/product -H "Content-Type: application/json" \
     *      --data `{"productId":1234, "name", "myProduct", "weight":10}`
     * @param body a JSON representation of the new composite
     * @return JSON representation of new product
     */
    @PostMapping(
            value = "/product",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Product> createProduct(@RequestBody Product body);

    /**
     * Usage: "curl ${HOST}:${PORT}/product/1
     * @param productId id of product
     * @return product data, else null
     */
    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json"
    )
    Mono<Product> getProduct(@PathVariable int productId,
                             @RequestParam(value = "delay", required = false, defaultValue = "0") int delay,
                             @RequestParam(value = "faultPercent", required = false, defaultValue = "0") int faultPercent);

    /**
     * Usage: "curl -X DELETE ${HOST}:${PORT}/product/1
     * @param productId id of product
     */
    @DeleteMapping(value = "/product/{productId}")
    Mono<Void> deleteProduct(@PathVariable int productId);
}
