# Building the Software

## Table of Contents

- [Build Project (Gradle)](#build-project-gradle)
- [Build Individual Service (gradle)](#build-indiviual-service-gradle)
- [Building Docker Images](#building-docker-images)

## Documentation
- [Readme](../README.md)
- [Version Information](VERSION.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)

---

## Build Project (Gradle)

1. Make sure you are in the ./ServiceTransformation directory (the home of the project)
2. build the project
```text
./gradlew build
```
```text
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-23T10:30:37.960-05:00  INFO 1135346 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-23T10:30:37.961-05:00  INFO 1135346 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-23T10:30:43.206-05:00  INFO 1135494 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-23T10:30:43.207-05:00  INFO 1135494 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-23T10:30:48.317-05:00  INFO 1135639 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-23T10:30:48.318-05:00  INFO 1135639 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-23T10:30:53.444-05:00  INFO 1135783 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-23T10:30:53.445-05:00  INFO 1135783 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.10/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 23s
33 actionable tasks: 16 executed, 17 up-to-date
```
---

## Build Indiviual Service (Gradle)

```shell
./gradlew build :product-service:build
```

```text
BUILD SUCCESSFUL in 2s
33 actionable tasks: 33 up-to-date
```
---

## Building Docker Images

Make sure that docker desktop is started. (Check the documentation for installing / setting up)

```shell
docker build -t product-composite-service product-composite-service/
docker build -t product-service product-service/
docker build -t recommendation-service recommendation-service/
docker build -t review-service review-service/
```
```text
[+] Building 1.4s (14/14) FINISHED                                                                                                                                                                                                                                                            docker:default
 => [internal] load build definition from Dockerfile                                                                                                                                                                                                                                                    0.0s
 => => transferring dockerfile: 500B                                                                                                                                                                                                                                                                    0.0s
 => [internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                                    0.2s
 => [internal] load .dockerignore                                                                                                                                                                                                                                                                       0.0s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [stage-1 1/6] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                                      0.0s
 => [internal] load build context                                                                                                                                                                                                                                                                       0.1s
 => => transferring context: 26.46MB                                                                                                                                                                                                                                                                    0.1s
 => CACHED [builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                              0.0s
 => [builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                        0.1s
 => [builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                    0.6s
 => CACHED [stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                                            0.0s 
 => CACHED [stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                 0.0s
 => CACHED [stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                           0.0s
 => CACHED [stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                        0.0s
 => [stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                         0.1s
 => exporting to image                                                                                                                                                                                                                                                                                  0.1s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.1s
 => => writing image sha256:ded981a1912cce343258d49733627d80839055cf339744105d4fac5261b0694b                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/product-composite-service                                                                                                                                                                                                                                            0.0s

View build details: docker-desktop://dashboard/build/default/default/qjkz4wv62omx31qbk73saxtoe

What's Next?
  View a summary of image vulnerabilities and recommendations → docker scout quickview
[+] Building 0.4s (14/14) FINISHED                                                                                                                                                                                                                                                            docker:default
 => [internal] load build definition from Dockerfile                                                                                                                                                                                                                                                    0.0s
 => => transferring dockerfile: 500B                                                                                                                                                                                                                                                                    0.0s
 => [internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                                    0.1s
 => [internal] load .dockerignore                                                                                                                                                                                                                                                                       0.0s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [builder 1/4] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                                      0.0s
 => [internal] load build context                                                                                                                                                                                                                                                                       0.1s
 => => transferring context: 26.45MB                                                                                                                                                                                                                                                                    0.1s
 => CACHED [stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                                            0.0s
 => CACHED [builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                              0.0s
 => CACHED [builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                 0.0s
 => CACHED [builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                             0.0s
 => CACHED [stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                 0.0s
 => CACHED [stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                           0.0s
 => CACHED [stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                        0.0s
 => CACHED [stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                  0.0s
 => exporting to image                                                                                                                                                                                                                                                                                  0.0s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.0s
 => => writing image sha256:877a11fee13896b5eb3c5632b28d3b2552f3c2bae4b7aa500aff881a8228c412                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/product-service                                                                                                                                                                                                                                                      0.0s

View build details: docker-desktop://dashboard/build/default/default/eogoh19uma9hsjcym57095t0f

What's Next?
  View a summary of image vulnerabilities and recommendations → docker scout quickview
[+] Building 1.8s (14/14) FINISHED                                                                                                                                                                                                                                                            docker:default
 => [internal] load build definition from Dockerfile                                                                                                                                                                                                                                                    0.0s
 => => transferring dockerfile: 500B                                                                                                                                                                                                                                                                    0.0s
 => [internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                                    0.1s
 => [internal] load .dockerignore                                                                                                                                                                                                                                                                       0.0s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [stage-1 1/6] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                                      0.0s
 => [internal] load build context                                                                                                                                                                                                                                                                       0.1s
 => => transferring context: 26.45MB                                                                                                                                                                                                                                                                    0.1s
 => CACHED [builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                              0.0s
 => [builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                        0.2s
 => [builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                    0.8s
 => CACHED [stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                                            0.0s 
 => CACHED [stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                 0.0s
 => CACHED [stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                           0.0s
 => CACHED [stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                        0.0s
 => [stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                         0.1s
 => exporting to image                                                                                                                                                                                                                                                                                  0.1s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.1s
 => => writing image sha256:24c35a97d0becadb542975877fb3f983d608b394b210cfad1c0e097c44f0a1e4                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/recommendation-service                                                                                                                                                                                                                                               0.0s

View build details: docker-desktop://dashboard/build/default/default/nzj2hyxtzpv2wjqt9potpxrvp

What's Next?
  View a summary of image vulnerabilities and recommendations → docker scout quickview
[+] Building 2.4s (14/14) FINISHED                                                                                                                                                                                                                                                            docker:default
 => [internal] load build definition from Dockerfile                                                                                                                                                                                                                                                    0.0s
 => => transferring dockerfile: 500B                                                                                                                                                                                                                                                                    0.0s
 => [internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                                    0.1s
 => [internal] load .dockerignore                                                                                                                                                                                                                                                                       0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [stage-1 1/6] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                                      0.0s
 => [internal] load build context                                                                                                                                                                                                                                                                       0.2s
 => => transferring context: 26.45MB                                                                                                                                                                                                                                                                    0.2s
 => CACHED [builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                              0.0s
 => [builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                        0.2s
 => [builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                    1.0s
 => CACHED [stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                                            0.0s 
 => CACHED [stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                 0.0s
 => CACHED [stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                           0.0s
 => CACHED [stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                        0.0s
 => [stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                         0.2s
 => exporting to image                                                                                                                                                                                                                                                                                  0.1s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.1s
 => => writing image sha256:b0ba3d1441aaa2a5c539b08a40e48864fa7e168897e5eaaa5654a9fac21b679d                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/review-service                                                                                                                                                                                                                                                       0.0s

View build details: docker-desktop://dashboard/build/default/default/tf91v7oro2t6xelms95na5bm4
```

### Verifying Images

```shell
docker images | grep "service"
```
```text

review-service              latest                    b0ba3d1441aa   2 minutes ago   492MB
recommendation-service      latest                    24c35a97d0be   2 minutes ago   492MB
product-composite-service   latest                    ded981a1912c   2 minutes ago   492MB
product-service             latest                    877a11fee138   4 minutes ago   492MB
```