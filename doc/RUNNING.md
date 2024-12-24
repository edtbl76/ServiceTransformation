# Running Services / Containers

## Contents

### Working w/ Containers

- [Containers (Intellij, Docker-Compose)](#starting-containers-intellij-w-docker-compose)
- [Containers (CLI, Docker-Compose)](#starting-docker-containers-from-the-cli-in-detached-mode)
- [Containers (CLI, Detached)](#starting-docker-containers-from-the-cli-in-detached-mode)
- [Containers (CLI)](#starting-docker-containers-from-the-cli)
- [Docker Tricks](#docker-tricks)

### Working w/ Services
- [Services (Intellij)](#starting-services-through-ide-tools)
- [Services (CLI)](#starting-services-w-java-on-the-cli)

## Documentation
- [Readme](../README.md)
- [Version Information](VERSION.md)
- [Release Notes](RELEASE.md)
- [Building](BUILD.md)
- [Testing Services](TESTING.md)
---

## Starting Containers (Intellij) w/ Docker-Compose

There are two ways to do this. 

### docker-compose.yml

In the margin of the docker-compose.yml file there is a
- double-green arrow next to the `services` directive. This will start **all** containers
- single-green arrows next to the individual containers that will start the those containers

### Intellij Services Tool
1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_Docker_**, select each of the Services, Rght-Click and select **Run**
3. (or) you can right-click the **_Docker_** menu heading and select **Run** to start all of the containers.


---
## Starting Containers (CLI) w/ Docker-Compose
```shell
docker-compose up -d
```
```text
[+] Running 5/5
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.1s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                         0.5s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                         0.5s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                         0.4s 
 ✔ Container servicetransformation-product-1            Started          
```

### Viewing Logs
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


### (Note: Intellij Services Tool)
Docker Containers started w/ docker-compose can be managed using the Alt-8 Services plugin
- logs
- console
- persistence
- variables
- and more!

### Shutting them down
```shell
docker-compose down
```

---

## Starting Docker Containers from the CLI in Detached Mode
```shell
docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name product-service-instance001 product-service
```
```text
c80a08eabc6a40b48dcb12f35503648f5a6178497927333cc47cfa5b43cb33a9
```
The output is the container hash

### Validate Detached Process
```shell
docker ps | grep "service"
```
NOTE: since I specified a name, it isn't randomly generated by the Docker Engine.
```text
(base) ~/IdeaProjects/ServiceTransformation git:[main]
docker ps
CONTAINER ID   IMAGE             COMMAND                  CREATED          STATUS          PORTS                    NAMES
c80a08eabc6a   product-service   "java org.springfram…"   28 seconds ago   Up 27 seconds   0.0.0.0:8080->8080/tcp   product-service-instance001
```

### Viewing Logs
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

### Stopping Detached Process
```shell
docker rm -f product-service-instance-001
docker ps | grep "service"
```
The name of the service is returned from the first command. Since we don't see the table output from `docker ps` we know that the container is no longer running.
```text
product-service-instance001
```

---
## Starting Docker Containers from the CLI

```shell
docker run --rm -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" product-service
```
```text
 :: Spring Boot ::                (v3.4.1)

2024-12-23T16:22:11.746Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Starting ProductServiceApplication using Java 17.0.13 with PID 1 (/application/BOOT-INF/classes started by root in /application)
2024-12-23T16:22:11.748Z DEBUG 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Running with Spring Boot v3.4.1, Spring v6.2.1
2024-12-23T16:22:11.749Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : The following 1 profile is active: "docker"
2024-12-23T16:22:12.762Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2024-12-23T16:22:13.087Z  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8080 (http)
2024-12-23T16:22:13.099Z  INFO 1 --- [           main] o.e.s.c.p.ProductServiceApplication      : Started ProductServiceApplication in 1.761 seconds (process running for 2.039)
```

To stop the container, just hit CTRL-C.

### Validating Docker Process
```shell
docker ps
```
```text
CONTAINER ID   IMAGE             COMMAND                  CREATED         STATUS         PORTS                    NAMES
312778e9cf91   product-service   "java org.springfram…"   3 minutes ago   Up 3 minutes   0.0.0.0:8080->8080/tcp   gracious_hellman
```

## Docker Tricks

### Getting only container names from docker ps
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

---



## Starting Services Through IDE Tools

This example uses Intellij's "Services" Tool

1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_SpringBoot_**, select each of the Services, Rght-Click and select **Run**
3. (or) you can usually right-click the **_SpringBoot_** menu heading and select **Run** to start all of the services.

---

## Starting Services w/ Java on the CLI

You can run these in the background or open up multiple terminal windows

1. Get to the home directory: **_ServiceTransformation_**
2. Run each of these commands from the home directory

```shell
java -jar product-compose-service/build/libs/*.jar &
java -jar product-service/build/libs/*.jar &
java -jar recommendation-service/build/libs/*.jar &
java -jar review-service/build/libs/*.jar &
```
NOTE:
- Even though these are running in the background, the output should display in the Terminal.
- You _can_ run them in a single window, but the output is a bit messy


Example:
```text
2024-12-22T21:48:33.824-05:00  INFO 735987 --- [           main] s.c.p.ProductCompositeServiceApplication 
: Started ProductCompositeServiceApplication in 1.592 seconds
```
The number after the word **INFO** is the terminal process.

```shell
(base) edmangini@pop-os:~/IdeaProjects/ServiceTransformation$ ps aux | grep 735987
edmangi+  735987  2.5  0.4 21594296 320252 pts/4 SNl  21:48   0:11 java -jar product-composite-service/build/libs/product-composite-service.jar product-composite-service/build/libs/product-composite-service-plain.jar
```

### Stopping Background Processes

Bring the service to the foreground and terminate it w/ CTRL-C
```shell
fg
java -jar product-service/build/libs/*.jar
^C2024-12-22T21:56:55.800-05:00  INFO 739147 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-22T21:56:55.805-05:00  INFO 739147 --- [ netty-shutdo
```
---
