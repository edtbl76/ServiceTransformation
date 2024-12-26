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
    public Product createProduct(Product body) {

        try {
            ProductEntity entity = mapper.apiToEntity(body);
            ProductEntity newEntity = repository.save(entity);

            log.debug("createProduct: entity created for productId: {}", body.getProductId());
            return mapper.entityToApi(newEntity);
        } catch (DuplicateKeyException e) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
        }
    }


    @Override
    public Product getProduct(int productId) {
        log.debug("/product returns found product for productId={}", productId);

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        ProductEntity entity = repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product response = mapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        log.debug("getProduct: found productId: {}", response.getProductId());
        return response;
    }

    @Override
    public void deleteProduct(int productId) {
        log.debug("deleteProduct: attempts to delete an entity with productId: {}", productId);
        repository.findByProductId(productId)
                .ifPresent(repository::delete);
    }
}
