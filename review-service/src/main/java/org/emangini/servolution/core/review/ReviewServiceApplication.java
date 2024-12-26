package org.emangini.servolution.core.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@ComponentScan("org.emangini")
public class ReviewServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReviewServiceApplication.class, args);

        System.out.println("Executing Context");

        String mysqlUri = context.getEnvironment().getProperty("spring.datasource.url");
        log.info("Connected to MySQL: {}", mysqlUri);

        String username = context.getEnvironment().getProperty("spring.datasource.username");
        log.info("Connected to MySQL as user: {}", username);

        // Validate if configuration exists
        if (mysqlUri == null || username == null) {
            log.error("Database configuration is missing. Check 'application.properties' or 'application.yml'.");
            System.exit(1);
        }

    }
}

