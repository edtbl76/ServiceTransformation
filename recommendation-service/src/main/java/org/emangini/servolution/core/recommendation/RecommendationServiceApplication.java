package org.emangini.servolution.core.recommendation;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.core.recommendation.persistence.RecommendationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.index.ReactiveIndexOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

@SpringBootApplication
@Slf4j
@ComponentScan("org.emangini")
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(RecommendationServiceApplication.class, args);

        String mongoDbHost = context.getEnvironment().getProperty("spring.data.mongodb.host");
        String mongoDbPort = context.getEnvironment().getProperty("spring.data.mongodb.port");
        log.info("Connected to MongoDB: {}:{}", mongoDbHost, mongoDbPort);

    }

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {

        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext
                = mongoTemplate.getConverter().getMappingContext();

        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
        ReactiveIndexOperations indexOps = mongoTemplate.indexOps(RecommendationEntity.class);

        resolver.resolveIndexFor(RecommendationEntity.class).forEach(indexOps::ensureIndex);
    }

}
