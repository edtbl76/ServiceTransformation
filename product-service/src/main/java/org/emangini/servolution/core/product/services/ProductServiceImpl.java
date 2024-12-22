package org.emangini.servolution.core.product.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.api.core.product.ProductService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        log.debug("/product returns found product for productId={}", productId);

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        if (productId == 13) {
            throw new NotFoundException("No product found for productId: " + productId);
        }

        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }

}
