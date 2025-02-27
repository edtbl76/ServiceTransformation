package org.emangini.servolution.core.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@ComponentScan("org.emangini")
public class ReviewServiceApplication {

    private final Integer threadPoolSize;
    private final Integer taskQueueSize;

    @Autowired
    public ReviewServiceApplication(
            @Value("${app.threadPoolSize:10}")
            Integer threadPoolSize,
            @Value("${app.queueCapacity:100}")
            Integer taskQueueSize) {
        this.threadPoolSize = threadPoolSize;
        this.taskQueueSize = taskQueueSize;
    }

    @Bean
    public Scheduler jdbcScheduler() {
        log.info("Creating JDBC Scheduler with threadPoolSize={}", threadPoolSize);
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReviewServiceApplication.class, args);

        String mysqlUri = context.getEnvironment().getProperty("spring.datasource.url");
        log.info("Connected to MySQL: {}", mysqlUri);

    }
}

