# Running Services in Containers

# Contents

- [Launching from CLI (docker-compose)](#launching-from-cli-docker-compose)
- [Launching from Intellij](#launching-from-intellij-services-tool)
- [Launching from docker-compose.yml (Intellij)](#launching-from-docker-composeyml-intellij)
- [Launching from the CLI (Detached)](#launching-from--the-cli-in-detached-mode)
- [Launching from the CLI](#launching-from-the-cli)

---

## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)
---




## Launching from CLI (docker-compose)
```shell
docker-compose up -d
```
```text
[+] Running 10/11
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                             0.0s 
 ✔ Container servicetransformation-auth-server-1        Healthy                                                                                                                                                                                                                                             6.1s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                             6.1s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                                                                                                             0.6s 
 ⠧ Container servicetransformation-mysql-1              Waiting                                                                                                                                                                                                                                             9.8s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                             6.2s 
 ✔ Container servicetransformation-gateway-1            Started                                                                                                                                                                                                                                             6.2s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                             6.8s 
 ✔ Container servicetransformation-review-1             Created                                                                                                                                                                                                                                             0.1s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                             6.8s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                             6.8s 
```

### Stopping / Shutting Down
```shell
docker-compose down
```

---
## Launching from Intellij Services tool
1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_Docker_**, select each of the Services, Rght-Click and select **Run**
3. (or) you can right-click the **_Docker_** menu heading and select **Run** to start all of the containers.

## Launching from `docker-compose.yml` (Intellij)

In the margin of the docker-compose.yml file there is a
- double-green arrow next to the `services` directive. This will start **all** containers
- single-green arrows next to the individual containers that will start the those containers

---

## Launching from  the CLI in Detached Mode
```shell
docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name product-service-instance001 product-service
```
```text
c80a08eabc6a40b48dcb12f35503648f5a6178497927333cc47cfa5b43cb33a9
```
The output is the container hash

#### Validate Detached Process
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

#### Stopping Detached Process
```shell
docker rm -f product-service-instance-001
docker ps | grep "service"
```
The name of the service is returned from the first command. Since we don't see the table output from `docker ps` we know that the container is no longer running.
```text
product-service-instance001
```

---
## Launching from the CLI

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

