# Running Services / Containers

## Contents
- [Starting Docker Containers from CLI](#starting-docker-containers-from-the-cli)
- [Starting Services Through IDE (Intellij)](#starting-services-through-ide-tools)
- [Starting Services w/ Java on the CLI](#starting-services-w-java-on-the-cli)

## Documentation
- [Readme](../README.md)
- [Version Information](VERSION.md)
- [Release Notes](RELEASE.md)
- [Building](BUILD.md)
- [Testing Services](TESTING.md)
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

---

### Starting Services Through IDE Tools

This example uses Intellij's "Services" Tool

1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_SpringBoot_**, select each of the Services, Rght-Click and select **Run**
3. (or) you can usually right-click the **_SpringBoot_** menu heading and select **Run** to start all of the services.

---

### Starting Services w/ Java on the CLI

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

### Killing Them Gracefully.

Bring the service to the foreground and terminate it w/ CTRL-C
```shell
fg
java -jar product-service/build/libs/*.jar
^C2024-12-22T21:56:55.800-05:00  INFO 739147 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-22T21:56:55.805-05:00  INFO 739147 --- [ netty-shutdo
```
---
