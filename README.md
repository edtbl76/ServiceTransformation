# ServiceTransformation

## Table of Contents
- [Release Notes](#release-notes)
- [Quickstart](#quickstart)
- [Links](#links)

## Documentation
- [Readme](README.md)
- [Building](./doc/BUILD.md)
- [Release Notes](./doc/RELEASE.md)
- [Running Services](./doc/RUNNING.md)
- [Testing Services](./doc/TESTING.md)
- [Support](./doc/SUPPORT.md)
---

## Release Notes



[Previous Releases](./doc/RELEASE.md)

---

## Quickstart

### All in One

```shell
./gradlew clean build && docker-compose build
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

## Links

NOTE: Environment must be up to reach these.

- [Eureka (Service Discovery)](#eureka)
- [OpenAPI](#openapi)
- [RabbitMQ (Messaging)](#rabbitmq)
- [Spring Actuator (HealthCheck)](#spring-actuator)
- [Zipkin (Tracing)](#zipkin)

### Eureka

Credentials
```text
user:username
pass:password
```

- [Eureka Administration](http://localhost:8761/)
- [Eureka XML Manifest (Direct)](http://localhost:8761/eureka/apps)
- [Eureka XML Manifest (Gateway)](https://localhost:8443/eureka/api/apps)

### OpenAPI

- Product Composite API Documentation 
- [(Web) - https://localhost:8443/openapi/swagger-ui.html](http://localhost:8080/openapi/swagger-ui.html)
- [(JSON) - http://localhost:8080/openapi/v3/api-docs](http://localhost:8080/openapi/v3/api-docs)


### RabbitMQ

- [RabbigMQ Admin Portal](http://localhost:15672/)
  - [Overview](http://localhost:15672/#/)
  - [Connections](http://localhost:15672/#/connections)
  - [Channels](http://localhost:15672/#/channels)
  - [Exchanges](http://localhost:15672/#/exchanges)
  - [Queues and Streams](http://localhost:15672/#/queues)
  - [Admin](http://localhost:15672/#/users)

Credentials
```text
user:guest
pass:guest
```


### Spring Actuator

- [/actuator](http://localhost:8080/actuator)
  - [/actuator/beans](http://localhost:8080/actuator/beans)
  - [/actuator/bindings](http://localhost:8080/actuator/bindings)
  - [/actuator/caches](http://localhost:8080/actuator/caches) 
  - [/actuator/channels](http://localhost:8080/actuator/channels)
  - [/actuator/conditions](http://localhost:8080/actuator/conditions)
  - [/actuator/configprops](http://localhost:8080/actuator/configprops)
  - [/actuator/env](http://localhost:8080/actuator/env)
  - [/actuator/functions](http://localhost:8080/actuator/functions)
  - [/actuator/health](http://localhost:8080/actuator/health)
  - [/actuator/heapdump](http://localhost:8080/actuator/heapdump)
  - [/actuator/info](http://localhost:8080/actuator/info)
  - [/actuator/integrationgraph](http://localhost:8080/actuator/integrationgraph)
  - [/actuator/loggers](http://localhost:8080/actuator/loggers)
  - [/actuator/mappings](http://localhost:8080/actuator/mappings)
  - [/actuator/threaddump](http://localhost:8080/actuator/threaddump)
  - [/actuator/metrics](http://localhost:8080/actuator/metrics)
  - [/actuator/sbom](http://localhost:8080/actuator/sbom)
  - [/actuator/scheduledtasks](http://localhost:8080/actuator/scheduledtasks)


### Zipkin

- [Zipkin UI](http://127.0.0.1:9411/zipkin/)
---






