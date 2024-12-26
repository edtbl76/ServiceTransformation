package org.emangini.servolution.core.product.services;

import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.core.product.persistence.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "serviceAddress", ignore = true)
    Product entityToApi(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProductEntity apiToEntity(Product api);
}
