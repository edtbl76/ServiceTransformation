# Release Notes

- [v2.1.0](#v210---openapi) [(v2.1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.1.0)
- [v2.0.0](#v200---docker-compose) [(v2 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v2.0.0)
- [v1.0.2](#v102---springboot-java-microservices) [(v1 Release)](https://github.com/edtbl76/ServiceTransformation/releases/tag/v1.0.2)
- [v1.0.1](#v101)
- [v1.0.0](#v100)


---

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