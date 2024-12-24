# ServiceTransformation v2.2.0

## Table of Contents
- [Version Information](./doc/VERSION.md)
- [Release Notes](#release-notes)
- [Quickstart](#quickstart)
- [OpenAPI](#openapi)

## Documentation
- [Build](./doc/BUILD.md)
- [Running](./doc/RUNNING.md)
- [Testing](./doc/TESTING.md)

---

## Release Notes

feature: persistence



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






