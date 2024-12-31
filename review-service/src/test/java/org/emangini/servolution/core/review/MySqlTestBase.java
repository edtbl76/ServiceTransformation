package org.emangini.servolution.core.review;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class MySqlTestBase {

    private static final String MYSQL_DOCKER_IMAGE_NAME = "mysql:8.0.33";

    private static final MySQLContainer<?> database =
            new MySQLContainer<>(DockerImageName.parse(MYSQL_DOCKER_IMAGE_NAME));

    // ensure the container is started before JUnit 5 code is executed.
    static {
        database.start();
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
}
