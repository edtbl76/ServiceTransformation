# ServiceTransformation v2.0.0

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

- Containerized Services (Using Docker)
  - docker-compose.yml
  - Dockerfile for all services
  - updated application.yml for all services to include docker spring-profile
- updated testRunner.sh to work w/ Docker by default 
- added doc folder and refactored documentation

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






