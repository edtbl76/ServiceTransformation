package org.emangini.servolution.core.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.emangini")
public class RecommendationServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(RecommendationServiceApplication.class, args);
        }
}
