# Release Notes

- [Version 3](#version-3)
- [Version 2](#version-2)
- [Version 1](#version-1)

---

# Version 3

- Service Discovery (Eureka)
---

- [v3.0.0](#v300---service-discovery-eureka) [(v3.0.0)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v3.0.0)

## v3.0.0 - Service Discovery (Eureka)

- **Service Discovery with Eureka**  
  All microservices are now integrated with Eureka for centralized service discovery.  
  Each service is configured to automatically register with Eureka upon startup. This simplifies inter-service communication and scalability.

- **Improved Properties Configuration**  
  Updated application properties across all microservices to enable seamless integration with Eureka.

- **Simplified Service Endpoints**  
  Service URLs within the product composite service have been simplified to leverage Eureka's service registry, improving maintainability and scalability.

- **Post-Construction Validation**  
  Beans now utilize a post-construction validation step to ensure the application context is properly initialized.



---

# Version 2

- Reactive Streams / Event-Driven Architecture (RabbitMQ, Kafka)
- Persistence (MongoDB, MySQL)
- OpenAPI (Contracts, API-Driven Development)
- Docker (Containerization)

---

- [v2.3.2](#v232---kafka-bug-fix-documentation-updates) [(v2.3.2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.3.2)
- [v2.3.0](#v230---reactive-streams-event-driven-arch-rabbitmq-kafka) [(v2.3.0 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.3.0)
- [v2.2.0](#v220---persistence-mongo-mysql) [(v2.2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.2.0)
- [v2.1.0](#v210---openapi) [(v2.1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.1.0)
- [v2.0.0](#v200---docker-compose) [(v2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.0.0)

## v2.3.2 - Kafka Bug Fix, Documentation Updates

## v2.3.0 - Reactive Streams, Event-Driven Arch (RabbitMQ, Kafka)

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

## v2.2.0 - Persistence (Mongo, MySQL)

[Project]
- created .env to store EV for persistence stores
- created gradle.properties to store versions
- updated build.gradle.kts
  - use variables for versions
  - moved directives into "allprojects" block
  - created blocks for all 4 microservices and the 3 non-composite microservices
  - added mapstruct for persistence mapping
- updated testRunner.sh
  - added seedTestData() to seed mongo, mysql for persistence tests
  - added recreateComposite() helper function for seed testing
- updated docker-compose
  - added implementations for mongo 7.0.16 and mysql 8.0.33
  - updated product, review and recommendation to connect to persistence containers

[API]
- updated build.gradle.kts to use variables for versions
- added NoArgsConstructor for ProductAggregate, RecommendationSummary, ReviewSummary
- added CreateProduct, DeleteProduct and usage descriptions for ProductCompositeService
- converted Product, Recommendation and Review records into POJOs.
  - Data, NoArgsConstructor, AllArgsConstructor lombok annotations
- created create(post), get(get) and delete(delete) mappings for Product, Recommendation, Review including usage descriptions
  
[ProductComposite]
- updated build.gradle.kts to use variables for versions
- added helper methods in ProductCompositeIntegration in order to generify the API intgrations
- added implementations for createProduct, deleteProduct for ProductCompositeServiceImpl; also added Slf4j
- updated OpenAPI definition in application.yml
- added tests (createComposite, deleteComposite) added test helper methods to generify test execution
  
[Product, Recommendation,Review]
- created *Entity, *Repository, *Mapper
- updated build.gradle.kts to use variables for versions
- updated *ServiceImpl
  - added create*, delete*, wired up for *Repository, *Mapper
- updated *ServiceApplication to initialize and log persisence connection on startup
- updated application.yml for mongo (Product, Recommendation) mysql (Review) connection
- added Mongo/MySQLDbTestBase, Persistence Tests, MapperTests
- updated *ServiceApplicationTests to support persistence


## v2.1.0 - OpenAPI
- API
  - added support for `springdoc-openapi-starter-common` to `build.gradle.kts`
  - adding OpenAPI documentation to `ProductCompositeService` interface.

- ProductCompositeService
  - added support for `springdoc-openapi-starter-webflux-ai` to `build.gradle.kts`
  - added OpenAPI Documentation Configuration to `ProductCompositeServiceApplication` and `application.yml`

- Util
  - added `BadRequestException` and updated `GlobalControllerExceptionHandler` to support it
  - added `NoArgsConstructor` to support to all exceptions via `lombok`

## v2.0.0 - Docker-Compose

- Containerized Services (Using Docker)
    - docker-compose.yml
    - Dockerfile for all services
    - updated application.yml for all services to include docker spring-profile
- updated testRunner.sh to work w/ Docker by default
- added doc folder and refactored documentation


---

# Version 1

- SpringBoot Java Microservices

---
- [v1.0.2](#v102---springboot-java-microservices) [(v1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v1.0.2)
- [v1.0.1](#v101)
- [v1.0.0](#v100)

## v1.0.2 - SpringBoot Java Microservices
- added support for automated landscape/end-to-end testing through testRunner.sh

## v1.0.1
- Updated Readme

## v.1.0.0

- Product, Recommendation, Review Services (SpringBoot)
- Product-Composite Service (SpringBoot)
- API layer
- Util Services for execption handling

- No Persistence (mocked)
- JUnit 5 / Mockito Unit Tests
- manual testing (curl, httpie)
- localized service start