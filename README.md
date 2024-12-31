# ServiceTransformation v2.3.0

## Table of Contents
- [Version Information](./doc/VERSION.md)
- [Release Notes](#release-notes)
- [Quickstart](#quickstart)
- [OpenAPI](#openapi)

## Documentation
- [Readme](../README.md)
- [Building](./doc/BUILD.md)
- [Release Notes](./doc/RELEASE.md)
- [Running Services](./doc/RUNNING.md)
- [Testing Services](./doc/TESTING.md)
- [Support](./doc/SUPPORT.md)
---

## Release Notes

feature: reactive streams and event-driven architecture

[Project]
- added spring cloud messaging dependencies v4.2.0
- documentation refactoring
- added messaging support and Spring Actuator / HealthCheck support for testRunner.sh
- added support for rabbitmq and kafka to docker-compose; created additional profiles for streamed partitioning 

[API]
- converted Java records to lombok-based classes for ProductAggregate, RecommendationSummary, ReviewSummary, ServiceAddresses
- updated APIs to support Project Reactor (Reactive/Asynchronous) for all 4 API services (ProductCompositeService, ProductService, RecommendationService, ReviewService)
- replaced NoArgsConstructors w/ custom for Product, Recommendation, Review Data POJOs
- created EventType (Create, Delete) to support event-driven architecture
- added custom Local/ZonedDateTime Serializers to solve bug w/ Jackson
- added EventProcessingException 

[Product-Composite]
- added Scheduler support for concurrency (SpringBootApplication, Integration layer)
  - @Lazy loaded into integration layer to avoid circular dependency issues
- added support for Spring Actuator HealthCheck API
- added Project Reactor (Reactive Streams) support to integration and implementation layers
- added spring cloud streams/messaging support for rabbitmq, and additional profiles for streamed partitioning and kafka=
- added tests for messaging/events
- updated tests for reactive operations

[Product, Recommendation, Review]
- updated Mongo to Reactive Mongo support (Product, Recommendation) 
- updated Entities to have custom String repr
- updated Repositories for reactive crud operations
- added Message Processor Configuration class
- updated *ServiceImpl for reactive code
- - added spring cloud streams/messaging support for rabbitmq, and additional profiles for streamed partitioning and kafka
- added tests for messaging/events
- updated tests for reactive code (StepVerifier)


[Previous Releases](./doc/RELEASE.md)

## Quickstart

### All in One

```shell
./gradlew build 
docker-compose build
./testRunner.sh start stop
```

### One at a time

1. Build Services
```shell
./gradlew build
```

2. Build Docker Images
```shell
docker-compose build
```
3. Validate images
```shell
docker images | grep "service"
```
4. Start containers (Optional)
Just a smoke test before you run the test suite
```shell
docker-compose up -d
docker-compose down
```
5. Run the test suite
```shell
./testRunner.sh start stop
```

## OpenAPI

- Product Composite API Documentation (Web): [http://localhost:8080/openapi/swagger-ui.html](http://localhost:8080/openapi/swagger-ui.html)
- Product Composite API Documentation (JSON): [http://localhost:8080/openapi/v3/api-docs](http://localhost:8080/openapi/v3/api-docs)
---






