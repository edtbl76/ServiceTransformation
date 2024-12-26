package org.emangini.servolution.core.product;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class MongoDbTestBase {

    public static final String MONGO_DOCKER_IMAGE_NAME = "mongo:7.0.16";

    private static final MongoDBContainer database =
            new MongoDBContainer(DockerImageName.parse(MONGO_DOCKER_IMAGE_NAME));

    static {
        database.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", database::getHost);
        registry.add("spring.data.mongodb.port", () -> database.getMappedPort(27017));
        registry.add("spring.data.mongodb.database", () -> "test");
    }
}
