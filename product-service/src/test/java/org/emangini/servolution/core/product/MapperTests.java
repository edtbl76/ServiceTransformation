package org.emangini.servolution.core.product;

import org.emangini.servolution.api.core.product.Product;
import org.emangini.servolution.core.product.persistence.ProductEntity;
import org.emangini.servolution.core.product.services.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapperTests {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        Product api = new Product(1, "name", 1, "serviceAddress");
        ProductEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getProductId(), entity.getProductId());
        assertEquals(api.getName(), entity.getName());
        assertEquals(api.getWeight(), entity.getWeight());

        Product apiMappedFromEntity = mapper.entityToApi(entity);

        assertEquals(api.getProductId(), apiMappedFromEntity.getProductId());
        assertEquals(api.getName(), apiMappedFromEntity.getName());
        assertEquals(api.getWeight(), apiMappedFromEntity.getWeight());
    }
}
