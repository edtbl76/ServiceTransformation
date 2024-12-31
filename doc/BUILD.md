# Building the Software

## Table of Contents

### Gradle
- [Build Project (Gradle)](#build-project-gradle)
- [Build Individual Service (gradle)](#build-indiviual-service-gradle)

### Docker
- [Building Docker Images (w/ docker-compose)](#building-docker-images-w-docker-compose)
- [Building Docker Images (w/ docker)](#building-docker-images)


## Documentation
- [Readme](../README.md)
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
> Task :product-composite-service:compileJava
Note: /home/edmangini/IdeaProjects/ServiceTransformation/product-composite-service/src/main/java/org/emangini/servolution/composite/product/services/ProductCompositeServiceImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-31T00:51:08.746-05:00  INFO 2569480 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-31T00:51:08.748-05:00  INFO 2569480 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2024-12-31T00:51:10.771-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2024-12-31T00:51:10.771-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 0 subscriber(s).
2024-12-31T00:51:10.771-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
2024-12-31T00:51:10.784-05:00  INFO 2569480 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-31T00:51:10.785-05:00  INFO 2569480 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2024-12-31T00:51:12.792-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2024-12-31T00:51:12.792-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 0 subscriber(s).
2024-12-31T00:51:12.793-05:00  INFO 2569480 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-31T00:51:21.883-05:00  INFO 2569710 --- [localhost:41659] org.mongodb.driver.cluster               : Exception in monitor thread while connecting to server localhost:41659

com.mongodb.MongoSocketReadException: Exception receiving message
        at com.mongodb.internal.connection.InternalStreamConnection.translateReadException(InternalStreamConnection.java:814)
        at com.mongodb.internal.connection.InternalStreamConnection.receiveResponseBuffers(InternalStreamConnection.java:862)
        at com.mongodb.internal.connection.InternalStreamConnection.receiveCommandMessageResponse(InternalStreamConnection.java:517)
        at com.mongodb.internal.connection.InternalStreamConnection.receive(InternalStreamConnection.java:469)
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitor.lookupServerDescription(DefaultServerMonitor.java:249)
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitor.run(DefaultServerMonitor.java:176)
Caused by: java.io.IOException: The connection to the server was closed
        at com.mongodb.internal.connection.netty.NettyStream$OpenChannelFutureListener.lambda$operationComplete$0(NettyStream.java:527)
        at io.netty.util.concurrent.DefaultPromise.notifyListener0(DefaultPromise.java:590)
        at io.netty.util.concurrent.DefaultPromise.notifyListenersNow(DefaultPromise.java:557)
        at io.netty.util.concurrent.DefaultPromise.notifyListeners(DefaultPromise.java:492)
        at io.netty.util.concurrent.DefaultPromise.setValue0(DefaultPromise.java:636)
        at io.netty.util.concurrent.DefaultPromise.setSuccess0(DefaultPromise.java:625)
        at io.netty.util.concurrent.DefaultPromise.trySuccess(DefaultPromise.java:105)
        at io.netty.channel.DefaultChannelPromise.trySuccess(DefaultChannelPromise.java:84)
        at io.netty.channel.AbstractChannel$CloseFuture.setClosed(AbstractChannel.java:1161)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.doClose0(AbstractChannel.java:753)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.close(AbstractChannel.java:729)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.close(AbstractChannel.java:619)
        at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.closeOnRead(AbstractNioByteChannel.java:105)
        at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:174)
        at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:833)

2024-12-31T00:51:23.853-05:00  INFO 2569710 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2024-12-31T00:51:23.853-05:00  INFO 2569710 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2024-12-31T00:51:23.854-05:00  INFO 2569710 --- [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.products.productsGroup'
2024-12-31T00:51:23.856-05:00  INFO 2569710 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-143924632.messageProcessor-in-0.errors' has 1 subscriber(s).
2024-12-31T00:51:23.857-05:00  INFO 2569710 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-143924632.messageProcessor-in-0.errors' has 0 subscriber(s).
2024-12-31T00:51:23.858-05:00  INFO 2569710 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-31T00:51:23.859-05:00  INFO 2569710 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2024-12-31T00:51:25.879-05:00  INFO 2569710 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2024-12-31T00:51:25.880-05:00  INFO 2569710 --- [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 0 subscriber(s).
2024-12-31T00:51:25.880-05:00  INFO 2569710 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-31T00:51:36.788-05:00  INFO 2570039 --- [localhost:37719] org.mongodb.driver.cluster               : Exception in monitor thread while connecting to server localhost:37719

com.mongodb.MongoSocketReadException: Exception receiving message
        at com.mongodb.internal.connection.InternalStreamConnection.translateReadException(InternalStreamConnection.java:814)
        at com.mongodb.internal.connection.InternalStreamConnection.receiveResponseBuffers(InternalStreamConnection.java:862)
        at com.mongodb.internal.connection.InternalStreamConnection.receiveCommandMessageResponse(InternalStreamConnection.java:517)
        at com.mongodb.internal.connection.InternalStreamConnection.receive(InternalStreamConnection.java:469)
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitor.lookupServerDescription(DefaultServerMonitor.java:249)
        at com.mongodb.internal.connection.DefaultServerMonitor$ServerMonitor.run(DefaultServerMonitor.java:176)
Caused by: java.io.IOException: The connection to the server was closed
        at com.mongodb.internal.connection.netty.NettyStream$OpenChannelFutureListener.lambda$operationComplete$0(NettyStream.java:527)
        at io.netty.util.concurrent.DefaultPromise.notifyListener0(DefaultPromise.java:590)
        at io.netty.util.concurrent.DefaultPromise.notifyListenersNow(DefaultPromise.java:557)
        at io.netty.util.concurrent.DefaultPromise.notifyListeners(DefaultPromise.java:492)
        at io.netty.util.concurrent.DefaultPromise.setValue0(DefaultPromise.java:636)
        at io.netty.util.concurrent.DefaultPromise.setSuccess0(DefaultPromise.java:625)
        at io.netty.util.concurrent.DefaultPromise.trySuccess(DefaultPromise.java:105)
        at io.netty.channel.DefaultChannelPromise.trySuccess(DefaultChannelPromise.java:84)
        at io.netty.channel.AbstractChannel$CloseFuture.setClosed(AbstractChannel.java:1161)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.doClose0(AbstractChannel.java:753)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.close(AbstractChannel.java:729)
        at io.netty.channel.AbstractChannel$AbstractUnsafe.close(AbstractChannel.java:619)
        at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.closeOnRead(AbstractNioByteChannel.java:105)
        at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:174)
        at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:833)

2024-12-31T00:51:38.772-05:00  INFO 2570039 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2024-12-31T00:51:38.772-05:00  INFO 2570039 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2024-12-31T00:51:38.772-05:00  INFO 2570039 --- [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.recommendations.recommendationsGroup'
2024-12-31T00:51:38.775-05:00  INFO 2570039 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-929738476.messageProcessor-in-0.errors' has 1 subscriber(s).
2024-12-31T00:51:38.775-05:00  INFO 2570039 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-929738476.messageProcessor-in-0.errors' has 0 subscriber(s).
2024-12-31T00:51:38.776-05:00  INFO 2570039 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-31T00:51:38.777-05:00  INFO 2570039 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2024-12-31T00:51:40.804-05:00  INFO 2570039 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2024-12-31T00:51:40.804-05:00  INFO 2570039 --- [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 0 subscriber(s).
2024-12-31T00:51:40.805-05:00  INFO 2570039 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2024-12-31T00:52:12.065-05:00  INFO 2570353 --- [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2024-12-31T00:52:12.066-05:00  INFO 2570353 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2024-12-31T00:52:12.071-05:00  INFO 2570353 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2024-12-31T00:52:12.075-05:00  INFO 2570353 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2024-12-31T00:52:12.076-05:00  INFO 2570353 --- [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2024-12-31T00:52:12.076-05:00  INFO 2570353 --- [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.reviews.reviewsGroup'
2024-12-31T00:52:12.081-05:00  INFO 2570353 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-88113725.messageProcessor-in-0.errors' has 1 subscriber(s).
2024-12-31T00:52:12.082-05:00  INFO 2570353 --- [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'application.rabbit-88113725.messageProcessor-in-0.errors' has 0 subscriber(s).
2024-12-31T00:52:12.083-05:00  INFO 2570353 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-31T00:52:12.085-05:00  INFO 2570353 --- [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2024-12-31T00:52:14.106-05:00  INFO 2570353 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2024-12-31T00:52:14.106-05:00  INFO 2570353 --- [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 0 subscriber(s).
2024-12-31T00:52:14.107-05:00  INFO 2570353 --- [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
2024-12-31T00:52:14.119-05:00  INFO 2570353 --- [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2024-12-31T00:52:14.120-05:00  INFO 2570353 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown initiated...
2024-12-31T00:52:14.122-05:00  INFO 2570353 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown completed.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.10/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 1m 17s
43 actionable tasks: 43 executed
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
## Building Docker Images w/ Docker Compose
```shell
docker-compose build
```
```text
[+] Building 5.3s (44/44) FINISHED                                                                                                                                                                                                                                                            docker:default
 => [recommendation internal] load build definition from Dockerfile                                                                                                                                                                                                                                     0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                    0.0s
 => [product internal] load build definition from Dockerfile                                                                                                                                                                                                                                            0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                    0.0s
 => [product-composite internal] load build definition from Dockerfile                                                                                                                                                                                                                                  0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                    0.0s
 => [product-composite internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                  0.2s
 => [review internal] load build definition from Dockerfile                                                                                                                                                                                                                                             0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                    0.0s
 => [recommendation internal] load .dockerignore                                                                                                                                                                                                                                                        0.0s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [product-composite internal] load .dockerignore                                                                                                                                                                                                                                                     0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [review internal] load .dockerignore                                                                                                                                                                                                                                                                0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [product internal] load .dockerignore                                                                                                                                                                                                                                                               0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                         0.0s
 => [product builder 1/4] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                              0.0s
 => [recommendation internal] load build context                                                                                                                                                                                                                                                        0.5s
 => => transferring context: 67.13MB                                                                                                                                                                                                                                                                    0.5s
 => [product-composite internal] load build context                                                                                                                                                                                                                                                     0.6s
 => => transferring context: 63.95MB                                                                                                                                                                                                                                                                    0.5s
 => [product internal] load build context                                                                                                                                                                                                                                                               0.9s
 => => transferring context: 67.13MB                                                                                                                                                                                                                                                                    0.5s
 => [review internal] load build context                                                                                                                                                                                                                                                                0.9s
 => => transferring context: 92.33MB                                                                                                                                                                                                                                                                    0.8s
 => CACHED [review builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                       0.0s
 => [recommendation builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                         0.5s
 => [product-composite builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                      0.7s
 => [product builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                0.3s
 => [review builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                 0.4s
 => [recommendation builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                     1.2s
 => [product builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                            1.5s
 => [product-composite builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                  1.5s
 => [review builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                             1.5s
 => CACHED [product stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                                    0.0s
 => [product stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                0.5s
 => [product stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                          0.3s
 => [product-composite stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                      0.4s
 => [review stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                                 0.4s
 => [recommendation stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                0.4s
 => [review stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                           0.2s
 => [product-composite stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                0.2s
 => [recommendation stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                          0.3s
 => CACHED [product stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                         0.0s
 => CACHED [product stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                   0.0s
 => CACHED [product stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                0.0s
 => [product stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                 0.3s
 => [review stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                        0.3s
 => [product-composite stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                             0.3s
 => [recommendation] exporting to image                                                                                                                                                                                                                                                                 0.5s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.4s
 => => writing image sha256:cbfe3dfde0ec2a101ea78a808e08e7fabba250fbc76db5a0e9e7b8daa9ee16fe                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/servicetransformation-recommendation                                                                                                                                                                                                                                 0.0s
 => [product] exporting to image                                                                                                                                                                                                                                                                        0.5s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.4s
 => => writing image sha256:aacd717fcd529f6759230be979d09a38767f3dbde99f1c5d1ea49fe4232997c2                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/servicetransformation-product                                                                                                                                                                                                                                        0.0s
 => [review stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                  0.2s
 => [product-composite stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                       0.2s
 => [review] exporting to image                                                                                                                                                                                                                                                                         0.6s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.5s
 => => writing image sha256:6a3a3f91f9d09f99f8c9b2740a26ecbfc1d57b0142a5d90de33a76b4e01c32bd                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/servicetransformation-review                                                                                                                                                                                                                                         0.0s
 => [product-composite] exporting to image                                                                                                                                                                                                                                                              0.6s
 => => exporting layers                                                                                                                                                                                                                                                                                 0.5s
 => => writing image sha256:7cb0b37afbdb73effbb2d4a5da7d891bbefaa237cc22f5bccca504a06444966c                                                                                                                                                                                                            0.0s
 => => naming to docker.io/library/servicetransformation-product-composite                                                                                                                                                                                                                              0.0s
```

### Validate
```shell
docker images | grep "service"
```
```text
servicetransformation-review              latest    d567ecca021f   2 hours ago   492MB
servicetransformation-recommendation      latest    188916db6519   2 hours ago   492MB
servicetransformation-product-composite   latest    295ead4b726b   2 hours ago   492MB
servicetransformation-product             latest    0908b74f34fa   2 hours ago   492MB
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
docker images | grep "servicetransformation"
```
```text
servicetransformation-product-composite   latest    7cb0b37afbdb   51 seconds ago   529MB
servicetransformation-recommendation      latest    cbfe3dfde0ec   51 seconds ago   533MB
servicetransformation-product             latest    aacd717fcd52   51 seconds ago   533MB
servicetransformation-review              latest    6a3a3f91f9d0   51 seconds ago   558MB
```
