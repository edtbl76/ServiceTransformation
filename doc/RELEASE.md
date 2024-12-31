# Release Notes

- [v2.2.0](#v220---persistence-mongo-mysql) [(v2.2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.2.0)
- [v2.1.0](#v210---openapi) [(v2.1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.1.0)
- [v2.0.0](#v200---docker-compose) [(v2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.0.0)
- [v1.0.2](#v102---springboot-java-microservices) [(v1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v1.0.2)
- [v1.0.1](#v101)
- [v1.0.0](#v100)


---
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