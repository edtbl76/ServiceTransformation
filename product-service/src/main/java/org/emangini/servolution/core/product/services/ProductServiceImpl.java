package org.emangini.servolution.core.product.services;


import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.product.ProductService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.emangini.servolution.core.product.persistence.ProductEntity;
import org.emangini.servolution.core.product.persistence.ProductRepository;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

import static java.time.Duration.ofSeconds;
import static java.util.logging.Level.FINE;

@RestController
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(
            ProductRepository repository,
            @Qualifier("productMapperImpl") ProductMapper mapper,
            ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Mono<Product> createProduct(Product body) {

        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }

        ProductEntity entity = mapper.apiToEntity(body);

        return repository.save(entity)
                .log(log.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        e -> new InvalidInputException(
                                "Duplicate key, Product Id: " + body.getProductId()))
                .map(mapper::entityToApi);
    }


    @Override
    public Mono<Product> getProduct(int productId, int delay, int faultPercent) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        log.info("calling getProduct for productId={}", productId);

        return repository.findByProductId(productId)
                // Chaos Code -- could be extracted and obfuscated.
                .map(productEntity -> throwErrorIfCircuitBreakerFault(productEntity, faultPercent))
                .delayElement(ofSeconds(delay))
                // back to normal flow of code
                .switchIfEmpty(Mono.error(new NotFoundException(
                        "No product found for productId: " + productId)))
                .log(log.getName(), FINE)
                .map(mapper::entityToApi)
                .map(this::setServiceAddress);
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
        log.debug("deleteProduct: attempts to delete an entity with productId: {}", productId);
        return repository.findByProductId(productId)
                .log(log.getName(), FINE)
                .map(repository::delete)
                .flatMap(voidMono -> voidMono);
    }

    /*
        Helper Methods
     */
    private ProductEntity throwErrorIfCircuitBreakerFault(ProductEntity entity, int faultPercent) {

        if (faultPercent == 0) {
            return entity;
        }

        int randomThreshold = getRandomNumber(1, 100);

        if (faultPercent > randomThreshold) {
            log.debug("No error occurred: {} < {}", faultPercent, randomThreshold);
        } else {
            log.debug("Circuit Breaker Fault: Error Occurred: {} >= {}", faultPercent, randomThreshold);
            throw new RuntimeException("Circuit Breaker Fault");
        }

        return entity;
    }

    private final Random randomNumber = new Random();

    private int getRandomNumber(int min, int max) {

        if (max < min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return randomNumber.nextInt((max - min) + 1) + min;
    }

    private Product setServiceAddress(Product product) {
        product.setServiceAddress(serviceUtil.getServiceAddress());
        return product;
    }
}
