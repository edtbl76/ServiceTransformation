# ServiceTransformation v2.1.0

## Table of Contents
- [Version Information](./doc/VERSION.md)
- [Release Notes](#release-notes)
- [Quickstart](#quickstart)

## Documentation
- [Build](./doc/BUILD.md)
- [Running](./doc/RUNNING.md)
- [Testing](./doc/TESTING.md)

---

## Release Notes

- API
  - added support for `springdoc-openapi-starter-common` to `build.gradle.kts`
  - adding OpenAPI documentation to `ProductCompositeService` interface. 

- ProductCompositeService
  - added support for `springdoc-openapi-starter-webflux-ai` to `build.gradle.kts`
  - added OpenAPI Documentation Configuration to `ProductCompositeServiceApplication` and `application.yml`

- Util
  - added `BadRequestException` and updated `GlobalControllerExceptionHandler` to support it
  - added `NoArgsConstructor` to support to all exceptions via `lombok`

[Previous Releases](./doc/RELEASE.md)

## Quickstart

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

---






