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
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)
---

## Build Project (Gradle)

1. Make sure you are in the ./ServiceTransformation directory (the home of the project)
2. build the project
```text
./gradlew build
```

NOTE: the output is from `./gradlew clean build`
```text
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:02:50.709-05:00  INFO 989459 --- [ionShutdownHook] o.s.c.n.e.s.EurekaServiceRegistry        : Unregistering application UNKNOWN with eureka with status DOWN
2025-01-05T13:02:50.711-05:00  INFO 989459 --- [ionShutdownHook] o.s.b.w.e.tomcat.GracefulShutdown        : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:02:50.712-05:00  INFO 989459 --- [tomcat-shutdown] o.s.b.w.e.tomcat.GracefulShutdown        : Graceful shutdown complete
2025-01-05T13:02:50.714-05:00  INFO 989459 --- [ionShutdownHook] o.s.c.n.e.server.EurekaServerBootstrap   : Shutting down Eureka Server..
2025-01-05T13:02:50.714-05:00  INFO 989459 --- [ionShutdownHook] c.n.eureka.DefaultEurekaServerContext    : Shutting down ...
2025-01-05T13:02:50.714-05:00  INFO 989459 --- [ionShutdownHook] c.n.eureka.DefaultEurekaServerContext    : Shut down
2025-01-05T13:02:50.714-05:00  INFO 989459 --- [ionShutdownHook] o.s.c.n.e.server.EurekaServerBootstrap   : Eureka Service is now shutdown...
2025-01-05T13:02:50.715-05:00  INFO 989459 --- [ionShutdownHook] c.n.eureka.DefaultEurekaServerContext    : Shutting down ...
2025-01-05T13:02:50.715-05:00  INFO 989459 --- [ionShutdownHook] c.n.eureka.DefaultEurekaServerContext    : Shut down
2025-01-05T13:02:50.721-05:00  INFO 989459 --- [ionShutdownHook] com.netflix.discovery.DiscoveryClient    : Shutting down DiscoveryClient ...
2025-01-05T13:02:50.722-05:00  INFO 989459 --- [ionShutdownHook] com.netflix.discovery.DiscoveryClient    : Completed shut down of DiscoveryClient
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:02:55.355-05:00  INFO 989600 --- [gateway] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:02:55.356-05:00  INFO 989600 --- [gateway] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete

> Task :product-composite-service:compileJava
Note: /home/edmangini/IdeaProjects/ServiceTransformation/product-composite-service/src/main/java/org/emangini/servolution/composite/product/services/ProductCompositeServiceImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:03:07.919-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] r$IntegrationBinderInboundChannelAdapter : stopped org.springframework.cloud.stream.binder.test.TestChannelBinder$IntegrationBinderInboundChannelAdapter@386cc1c4
2025-01-05T13:03:07.921-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product-composite.192487022.forwardedHeaderTransformer-in-0.errors' has 1 subscriber(s).
2025-01-05T13:03:07.922-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product-composite.192487022.forwardedHeaderTransformer-in-0.errors' has 0 subscriber(s).
2025-01-05T13:03:07.924-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:03:07.925-05:00  INFO 989752 --- [product-composite] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2025-01-05T13:03:09.940-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2025-01-05T13:03:09.940-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'product-composite.errorChannel' has 0 subscriber(s).
2025-01-05T13:03:09.940-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
2025-01-05T13:03:09.955-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2025-01-05T13:03:09.955-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2025-01-05T13:03:09.955-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.forwardedHeaderTransformer-in-0.anonymous.EXwdbjSmShyMe3W3Y_JCZw'
2025-01-05T13:03:09.960-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product-composite.rabbit-1383664466.forwardedHeaderTransformer-in-0.errors' has 1 subscriber(s).
2025-01-05T13:03:09.961-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product-composite.rabbit-1383664466.forwardedHeaderTransformer-in-0.errors' has 0 subscriber(s).
2025-01-05T13:03:09.961-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:03:09.961-05:00  INFO 989752 --- [product-composite] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2025-01-05T13:03:11.967-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2025-01-05T13:03:11.968-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'product-composite.errorChannel' has 0 subscriber(s).
2025-01-05T13:03:11.968-05:00  INFO 989752 --- [product-composite] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:03:24.361-05:00  INFO 992058 --- [product] [localhost:45505] org.mongodb.driver.cluster               : Exception in monitor thread while connecting to server localhost:45505

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

2025-01-05T13:03:26.328-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2025-01-05T13:03:26.328-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2025-01-05T13:03:26.328-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.products.productsGroup'
2025-01-05T13:03:26.333-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product.rabbit-353829212.messageProcessor-in-0.errors' has 1 subscriber(s).
2025-01-05T13:03:26.334-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'product.rabbit-353829212.messageProcessor-in-0.errors' has 0 subscriber(s).
2025-01-05T13:03:26.335-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:03:26.336-05:00  INFO 992058 --- [product] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2025-01-05T13:03:28.351-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2025-01-05T13:03:28.352-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'product.errorChannel' has 0 subscriber(s).
2025-01-05T13:03:28.352-05:00  INFO 992058 --- [product] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:03:40.501-05:00  INFO 992458 --- [recommendation] [localhost:46839] org.mongodb.driver.cluster               : Exception in monitor thread while connecting to server localhost:46839

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

2025-01-05T13:03:42.489-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2025-01-05T13:03:42.489-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2025-01-05T13:03:42.489-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.recommendations.recommendationsGroup'
2025-01-05T13:03:42.497-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'recommendation.rabbit-2098612181.messageProcessor-in-0.errors' has 1 subscriber(s).
2025-01-05T13:03:42.498-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'recommendation.rabbit-2098612181.messageProcessor-in-0.errors' has 0 subscriber(s).
2025-01-05T13:03:42.500-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:03:42.501-05:00  INFO 992458 --- [recommendation] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2025-01-05T13:03:44.520-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2025-01-05T13:03:44.520-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'recommendation.errorChannel' has 0 subscriber(s).
2025-01-05T13:03:44.521-05:00  INFO 992458 --- [recommendation] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
2025-01-05T13:04:15.822-05:00  INFO 992814 --- [review] [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2025-01-05T13:04:15.823-05:00  INFO 992814 --- [review] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2025-01-05T13:04:15.828-05:00  INFO 992814 --- [review] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2025-01-05T13:04:15.833-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Waiting for workers to finish.
2025-01-05T13:04:15.833-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.a.r.l.SimpleMessageListenerContainer : Successfully waited for workers to finish.
2025-01-05T13:04:15.833-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.i.a.i.AmqpInboundChannelAdapter      : stopped bean 'inbound.reviews.reviewsGroup'
2025-01-05T13:04:15.838-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'review.rabbit-1599365427.messageProcessor-in-0.errors' has 1 subscriber(s).
2025-01-05T13:04:15.839-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.c.stream.binder.BinderErrorChannel   : Channel 'review.rabbit-1599365427.messageProcessor-in-0.errors' has 0 subscriber(s).
2025-01-05T13:04:15.840-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2025-01-05T13:04:15.842-05:00  INFO 992814 --- [review] [ netty-shutdown] o.s.b.w.embedded.netty.GracefulShutdown  : Graceful shutdown complete
2025-01-05T13:04:17.865-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : Removing {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2025-01-05T13:04:17.865-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.i.channel.PublishSubscribeChannel    : Channel 'review.errorChannel' has 0 subscriber(s).
2025-01-05T13:04:17.866-05:00  INFO 992814 --- [review] [ionShutdownHook] o.s.i.endpoint.EventDrivenConsumer       : stopped bean '_org.springframework.integration.errorLogger'
2025-01-05T13:04:17.878-05:00  INFO 992814 --- [review] [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2025-01-05T13:04:17.878-05:00  INFO 992814 --- [review] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown initiated...
2025-01-05T13:04:17.881-05:00  INFO 992814 --- [review] [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Shutdown completed.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.10/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 1m 45s
67 actionable tasks: 67 executed
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
[+] Building 7.0s (75/75) FINISHED                                                                                                                                                                                                                                                                docker:default
 => [auth-server internal] load build definition from Dockerfile                                                                                                                                                                                                                                            0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [eureka internal] load build definition from Dockerfile                                                                                                                                                                                                                                                 0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [product-composite internal] load metadata for docker.io/library/amazoncorretto:17                                                                                                                                                                                                                      0.8s
 => [recommendation internal] load build definition from Dockerfile                                                                                                                                                                                                                                         0.0s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [review internal] load build definition from Dockerfile                                                                                                                                                                                                                                                 0.1s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [product internal] load build definition from Dockerfile                                                                                                                                                                                                                                                0.1s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [auth-server auth] library/amazoncorretto:pull token for registry-1.docker.io                                                                                                                                                                                                                           0.0s
 => [eureka internal] load .dockerignore                                                                                                                                                                                                                                                                    0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [review internal] load .dockerignore                                                                                                                                                                                                                                                                    0.0s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [auth-server internal] load .dockerignore                                                                                                                                                                                                                                                               0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [product internal] load .dockerignore                                                                                                                                                                                                                                                                   0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [recommendation internal] load .dockerignore                                                                                                                                                                                                                                                            0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [product-composite builder 1/4] FROM docker.io/library/amazoncorretto:17@sha256:d14414e3e0c511903957dbd037ff69999f6164820eeea7b46afd62e98ac4d6eb                                                                                                                                                        0.0s
 => [review internal] load build context                                                                                                                                                                                                                                                                    1.1s
 => => transferring context: 111.23MB                                                                                                                                                                                                                                                                       0.9s
 => [recommendation internal] load build context                                                                                                                                                                                                                                                            1.1s
 => => transferring context: 113.51MB                                                                                                                                                                                                                                                                       1.0s
 => [auth-server internal] load build context                                                                                                                                                                                                                                                               0.5s
 => => transferring context: 52.61MB                                                                                                                                                                                                                                                                        0.5s
 => [product internal] load build context                                                                                                                                                                                                                                                                   1.2s
 => => transferring context: 113.50MB                                                                                                                                                                                                                                                                       1.0s
 => [eureka internal] load build context                                                                                                                                                                                                                                                                    1.1s
 => => transferring context: 61.36MB                                                                                                                                                                                                                                                                        0.7s
 => CACHED [product-composite builder 2/4] WORKDIR extracted                                                                                                                                                                                                                                                0.0s
 => [auth-server builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                0.5s
 => [eureka builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                     0.4s
 => [auth-server builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                            1.1s
 => [review builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                     0.4s
 => [recommendation builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                             0.5s
 => [product builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                    0.5s
 => [review builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                 1.8s
 => [eureka builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                 1.7s
 => [product builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                2.1s
 => [recommendation builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                         2.1s
 => CACHED [product-composite stage-1 2/6] WORKDIR application                                                                                                                                                                                                                                              0.0s
 => CACHED [auth-server stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                         0.0s
 => CACHED [auth-server stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                   0.0s
 => CACHED [auth-server stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                0.0s
 => CACHED [auth-server stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                          0.0s
 => [auth-server] exporting to image                                                                                                                                                                                                                                                                        0.0s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.0s
 => => writing image sha256:c41e1b35ee4c794a447227683be1c376445cf373bf56d12e55d995d74bf276fb                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-auth-server                                                                                                                                                                                                                                        0.0s
 => [gateway internal] load build definition from Dockerfile                                                                                                                                                                                                                                                0.1s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [product-composite internal] load build definition from Dockerfile                                                                                                                                                                                                                                      0.1s
 => => transferring dockerfile: 507B                                                                                                                                                                                                                                                                        0.0s
 => [gateway internal] load .dockerignore                                                                                                                                                                                                                                                                   0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [product-composite internal] load .dockerignore                                                                                                                                                                                                                                                         0.1s
 => => transferring context: 2B                                                                                                                                                                                                                                                                             0.0s
 => [gateway internal] load build context                                                                                                                                                                                                                                                                   0.4s
 => => transferring context: 55.82MB                                                                                                                                                                                                                                                                        0.3s
 => [product-composite internal] load build context                                                                                                                                                                                                                                                         0.7s
 => => transferring context: 116.56MB                                                                                                                                                                                                                                                                       0.4s
 => CACHED [eureka stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                              0.0s
 => CACHED [eureka stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                        0.0s
 => CACHED [eureka stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                     0.0s
 => CACHED [eureka stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                               0.0s
 => CACHED [review stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                              0.0s
 => CACHED [review stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                        0.0s
 => CACHED [review stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                     0.0s
 => [eureka] exporting to image                                                                                                                                                                                                                                                                             0.1s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.0s
 => => writing image sha256:c0e149bac873752dc589fc568d642c45a62b9677aa54284914096db0fc0672c8                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-eureka                                                                                                                                                                                                                                             0.0s
 => [gateway builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                                    0.3s
 => [review stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                      0.3s
 => [gateway builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                                1.2s
 => [review] exporting to image                                                                                                                                                                                                                                                                             0.4s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.2s
 => => writing image sha256:e5baef5383036248218163c511a286189838f1b9bcebd7c572fd531b5b70b899                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-review                                                                                                                                                                                                                                             0.0s
 => CACHED [product stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                             0.0s
 => CACHED [product stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                       0.0s
 => CACHED [recommendation stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                             0.0s
 => [product-composite builder 3/4] ADD ./build/libs/*.jar app.jar                                                                                                                                                                                                                                          0.4s
 => CACHED [recommendation stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                      0.0s
 => CACHED [recommendation stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                0.0s
 => CACHED [recommendation stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                             0.0s
 => [recommendation stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                              0.4s
 => [product stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                                     0.3s
 => [product] exporting to image                                                                                                                                                                                                                                                                            0.4s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.2s
 => => writing image sha256:11eb6e7e7e7c5a96c302dbf27d5993f055f982094ce54b1aa134b102debbc921                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-product                                                                                                                                                                                                                                            0.0s
 => [recommendation] exporting to image                                                                                                                                                                                                                                                                     0.4s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.2s
 => => writing image sha256:249ec39bb9cc0aeeeac8220f2f09057428ab38a31be59e0821f85b66398397b4                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-recommendation                                                                                                                                                                                                                                     0.0s
 => [product-composite builder 4/4] RUN java -Djarmode=layertools -jar app.jar extract                                                                                                                                                                                                                      1.4s
 => CACHED [gateway stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                             0.0s
 => CACHED [gateway stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                                       0.0s
 => CACHED [gateway stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                                    0.0s
 => CACHED [gateway stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                              0.0s
 => [gateway] exporting to image                                                                                                                                                                                                                                                                            0.0s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.0s
 => => writing image sha256:14ebf52362d1be7e559352ad132eacca3e01a93c8836f6f82aadc403298ec98a                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-gateway                                                                                                                                                                                                                                            0.0s
 => CACHED [product-composite stage-1 3/6] COPY --from=builder extracted/dependencies/ ./                                                                                                                                                                                                                   0.0s
 => CACHED [product-composite stage-1 4/6] COPY --from=builder extracted/spring-boot-loader/ ./                                                                                                                                                                                                             0.0s
 => CACHED [product-composite stage-1 5/6] COPY --from=builder extracted/snapshot-dependencies/ ./                                                                                                                                                                                                          0.0s
 => [product-composite stage-1 6/6] COPY --from=builder extracted/application/ ./                                                                                                                                                                                                                           0.1s
 => [product-composite] exporting to image                                                                                                                                                                                                                                                                  0.1s
 => => exporting layers                                                                                                                                                                                                                                                                                     0.1s
 => => writing image sha256:a4bbfd4f4abe3c418b5469d9690a60c1788ce854b253ffba79a998f30024d7e4                                                                                                                                                                                                                0.0s
 => => naming to docker.io/library/servicetransformation-product-composite                                                                                                                                                                                                                                  0.0s
```

### Validate
```shell
docker images | grep "servicetransformation"
```
```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
docker images | grep "service"
servicetransformation-product-composite   latest             a4bbfd4f4abe   About a minute ago   582MB
servicetransformation-recommendation      latest             249ec39bb9cc   About a minute ago   579MB
servicetransformation-product             latest             11eb6e7e7e7c   About a minute ago   579MB
servicetransformation-review              latest             e5baef538303   About a minute ago   577MB
servicetransformation-gateway             latest             14ebf52362d1   11 hours ago         521MB
servicetransformation-eureka              latest             c0e149bac873   11 hours ago         527MB
servicetransformation-auth-server         latest             c41e1b35ee4c   11 hours ago         518MB
servicetransformation-recommendation-p2   latest             4dcf1385fb6d   3 days ago           533MB
servicetransformation-product-p2          latest             e68dfb6859b0   3 days ago           533MB
servicetransformation-review-p2           latest             b34712a87246   3 days ago           558MB
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
