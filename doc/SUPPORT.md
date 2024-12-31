# Support


## Contents

- [Docker](#docker)
  - [Validating Docker Process](#validating-docker-process)
  - [Filtering container names from 'docker ps'](#filtering-containers-names-from-docker-ps)
  - [Pruning all images](#pruning-all-images)
  - [Viewing Logs](#viewing-logs-in-docker)
- [Docker-Compose](#docker-compose)
  - [Viewing Logs](#viewing-logs-in-docker-compose)

## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)

---

## Docker

### Validating Docker Process
```shell
docker ps
```
```text
CONTAINER ID   IMAGE             COMMAND                  CREATED         STATUS         PORTS                    NAMES
312778e9cf91   product-service   "java org.springfram…"   3 minutes ago   Up 3 minutes   0.0.0.0:8080->8080/tcp   gracious_hellman
```
---

### Filtering Containers Names From Docker PS
```shell
docker ps --format {{.Names}}
```
```text
servicetransformation-recommendation-1
servicetransformation-product-1
servicetransformation-review-1
servicetransformation-product-composite-1
```

### Pruning all images
```shell
docker image prune -a
```

```text
WARNING! This will remove all images without at least one container associated to them.
Are you sure you want to continue? [y/N] y
Deleted Images:
deleted: sha256:295ead4b726bd5c09b86415cbf8f86964a5dca1bb376083d2725356dd716f839
deleted: sha256:0908b74f34fae9ebb9bb905f0d2d6b2dc3136053233242f305c884637e227631
untagged: servicetransformation-review:latest
deleted: sha256:4b9338d83eee89e55f48aa4c3070ee1744e3ce293d2b2ba5ceff620d44e3790a
deleted: sha256:188916db65191902328391b79dab4bb9b93654ba7f0597e771392626d6aaf1e4
untagged: servicetransformation-product-composite:latest
deleted: sha256:dd7cb2f23566bf7c3a6934d5aedf86f0f81baf213f4c0841c5290fb127a22cd7
deleted: sha256:d567ecca021f370c1910b335e7b9539e1a82019e19d7cd6983661c7296642e84
untagged: servicetransformation-product:latest
deleted: sha256:57aca9c31ee37cf7cf28bcca40ebcf2b0393b8c6857adc8e39d353e973a74ef1
untagged: servicetransformation-recommendation:latest
deleted: sha256:0224ee5b31466e063a5b8ea701afa9d0481ce93aa610b7727997e6a5f89b8237

Total reclaimed space: 0B
```

### Viewing Logs in Docker
```shell
docker logs product-service-instance001 -f
```
```text

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.1)

2024-12-23T17:19:55.012Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Starting ProductServiceApplication using Java 17.0.13 with PID 1 (/application/BOOT-INF/classes started by root in /application)
2024-12-23T17:19:55.015Z DEBUG 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Running with Spring Boot v3.4.1, Spring v6.2.1
2024-12-23T17:19:55.015Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : The following 1 profile is active: "docker"
2024-12-23T17:19:55.956Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2024-12-23T17:19:56.244Z  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080 (http)
2024-12-23T17:19:56.256Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Started ProductServiceApplication in 1.586 seconds (process running for 1.845)
```


---

## Docker-Compose

### Viewing Logs in docker-compose
```shell
docker-compose logs -f
```
```text
review-1             | ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
review-1             |  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
review-1             |   '  |____| .__|_| |_|_| |_\__, | / / / /
review-1             |  =========|_|==============|___/=/_/_/_/
review-1             | 
review-1             |  :: Spring Boot ::                (v3.4.1)
review-1             | 
recommendation-1     | 2024-12-23T17:44:36.264Z DEBUG 1 --- [           main] e.s.c.r.RecommendationServiceApplication : Running with Spring Boot v3.4.1, Spring v6.2.1
recommendation-1     | 2024-12-23T17:44:36.265Z  INFO 1 --- [           main] e.s.c.r.RecommendationServiceApplication : The following 1 profile is active: "docker"
recommendation-1     | 2024-12-23T17:44:37.775Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
recommendation-1     | 2024-12-23T17:44:38.237Z  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080 (http)
recommendation-1     | 2024-12-23T17:44:38.254Z  INFO 1 --- [           main] e.s.c.r.RecommendationServiceApplication : Started RecommendationServiceApplication in 2.463 seconds (process running for 2.736)
review-1             | 2024-12-23T17:44:36.446Z  INFO 1 --- [           main] o.e.s.c.review.ReviewServiceApplication  : Starting ReviewServiceApplication using Java 17.0.13 with PID 1 (/application/BOOT-INF/classes started by root in /application)
review-1             | 2024-12-23T17:44:36.451Z DEBUG 1 --- [           main] o.e.s.c.review.ReviewServiceApplication  : Running with Spring Boot v3.4.1, Spring v6.2.1
review-1             | 2024-12-23T17:44:36.451Z  INFO 1 --- [           main] o.e.s.c.review.ReviewServiceApplication  : The following 1 profile is active: "docker"
review-1             | 2024-12-23T17:44:37.890Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
review-1             | 2024-12-23T17:44:38.404Z  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080 (http)
review-1             | 2024-12-23T17:44:38.418Z  INFO 1 --- [           main] o.e.s.c.review.ReviewServiceApplication  : Started ReviewServiceApplication in 2.427 seconds (process running for 2.759)
```
