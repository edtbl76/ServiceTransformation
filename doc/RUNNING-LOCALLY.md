# Running Services Locally

## Contents
- [Launching Services in Intellij](#launching-services-in-intellij)
- [Launching Services on the CLI](#launching-services-on-the-cli)

---
## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)
---


## Launching Services in Intellij


1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_SpringBoot_**, select each of the Services, Rght-Click and select **Run**
3. (or) you can usually right-click the **_SpringBoot_** menu heading and select **Run** to start all of the services.

---

## Launching Services on the CLI

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
