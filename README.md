# ServiceTransformation v1.0.2

## Description
- Simple Services (local build, local execution)
- No Persistence
- Manual Tests w/ httpie
- Automated Tests w/ testRunner.sh

---

---

## Version Information

### Gradle

```text
./gradlew -v

------------------------------------------------------------
Gradle 8.10
------------------------------------------------------------

Build time:    2024-08-14 11:07:45 UTC
Revision:      fef2edbed8af1022cefaf44d4c0514c5f89d7b78

Kotlin:        1.9.24
Groovy:        3.0.22
Ant:           Apache Ant(TM) version 1.10.14 compiled on August 16 2023
Launcher JVM:  17.0.7 (Amazon.com Inc. 17.0.7+7-LTS)
Daemon JVM:    /home/edmangini/.sdkman/candidates/java/17.0.7-amzn 
(no JDK specified, using current Java home)
OS:            Linux 6.9.3-76060903-generic amd64
```

## Java

```text
java -version
openjdk version "17.0.7" 2023-04-18 LTS
OpenJDK Runtime Environment Corretto-17.0.7.7.1 (build 17.0.7+7-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.7.7.1 
(build 17.0.7+7-LTS, mixed mode, sharing)
```

## Spring Framework
```text
SpringBoot: 3.4.1
```

---

---

## Build the project

1. Make sure you are in the ./ServiceTransformation directory (the home of the project)
2. build the project
```text
./gradlew build
```

**Expected Output**
```text

BUILD SUCCESSFUL in 22s
33 actionable tasks: 10 executed, 23 up-to-date
```

**NOTE**
This will only work w/ Gradle 8. 
```text
Deprecated Gradle features were used in this build, 
making it incompatible with Gradle 9.0.
```
---

---

## Launch Microservices

### Intellij Services

1. Go to Tools --> Services (usually Alt-8 HotKey)
2. Under **_SpringBoot_**, select each of the Services, Rght-Click and select **Run**

(This is my preferred View)


### Intellij (Directory)

1. Right click on the service you want to launch
2. Navigate to **_src/main/java/org/emangini/servolution/core/\<service_name\>_**
3. Right click on **_\<ServiceName\>ServiceApplication_**
4. Select **_Run \<ServiceName\>...main()_** 

Output will show in the run terminal

### CLI

You can run these in the background or open up multiple terminal windows

1. Get to the home directory: **_ServiceTransformation_**
2. Run each of these commands from the home directory

```text
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

#### Killing Them Gracefully. 

Bring the service to the foreground and terminate it w/ CTRL-C
```shell
fg
java -jar product-service/build/libs/*.jar
^C2024-12-22T21:56:55.800-05:00  INFO 739147 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-22T21:56:55.805-05:00  INFO 739147 --- [ netty-shutdo
```
---

---

## Test (Manually)

### Confirm you can retrieve a record (curl) 

```shell
curl http://localhost:7000/product-composite/1 -s | jq
```

#### Output
```json
{
  "productId": 1,
  "name": "name-1",
  "weight": 123,
  "recommendations": [
    {
      "recommendationId": 1,
      "author": "Author 1",
      "rate": 1
    },
    {
      "recommendationId": 2,
      "author": "Author 2",
      "rate": 2
    },
    {
      "recommendationId": 3,
      "author": "Author 3",
      "rate": 3
    }
  ],
  "reviews": [
    {
      "reviewId": 1,
      "author": "Author 1",
      "subject": "Subject 1"
    },
    {
      "reviewId": 2,
      "author": "Author 2",
      "subject": "Subject 2"
    },
    {
      "reviewId": 3,
      "author": "Author 3",
      "subject": "Subject 3"
    }
  ],
  "serviceAddresses": {
    "compositeAddress": "pop-os/127.0.1.1:7000",
    "productAddress": "pop-os/127.0.1.1:7001",
    "reviewAddress": "pop-os/127.0.1.1:7003",
    "recommendationAddress": "pop-os/127.0.1.1:7002"
  }
}
```
---

### Confirm you can receive a record (Httpie)

This is my preference because it shows the header and pretty-prints. 

```shell
http http://localhost:7000/product-composite/1 --unsorted
```

#### Output
```shell
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 596

{
    "productId": 1,
    "name": "name-1",
    "weight": 123,
    "recommendations": [
        {
            "recommendationId": 1,
            "author": "Author 1",
            "rate": 1
        },
        {
            "recommendationId": 2,
            "author": "Author 2",
            "rate": 2
        },
        {
            "recommendationId": 3,
            "author": "Author 3",
            "rate": 3
        }
    ],
    "reviews": [
        {
            "reviewId": 1,
            "author": "Author 1",
            "subject": "Subject 1"
        },
        {
            "reviewId": 2,
            "author": "Author 2",
            "subject": "Subject 2"
        },
        {
            "reviewId": 3,
            "author": "Author 3",
            "subject": "Subject 3"
        }
    ],
    "serviceAddresses": {
        "compositeAddress": "pop-os/127.0.1.1:7000",
        "productAddress": "pop-os/127.0.1.1:7001",
        "reviewAddress": "pop-os/127.0.1.1:7003",
        "recommendationAddress": "pop-os/127.0.1.1:7002"
    }
}
```
---

### Confirm NotFound

#### 1. Verify that a 404 (Not Found) is returned for a non existing productId
```shell
http http://localhost:7000/product-composite/13 --unsorted;
```
#### Output
```json
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 162

{
    "timestamp": "2024-12-23T00:28:16.620539873-05:00",
    "path": "/product-composite/13",
    "message": "No product found for productId: 13",
    "status": 404,
    "error": "Not Found"
}
```


#### 2. Verify that there aren't any recommendations for productId 113
```shell
http http://localhost:7000/product-composite/113 --unsorted;
```

#### Output
```json
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 424

{
  "productId": 113,
  "name": "name-113",
  "weight": 123,
  "recommendations": [],
  "reviews": [
    {
      "reviewId": 1,
      "author": "Author 1",
      "subject": "Subject 1"
    },
    {
      "reviewId": 2,
      "author": "Author 2",
      "subject": "Subject 2"
    },
    {
      "reviewId": 3,
      "author": "Author 3",
      "subject": "Subject 3"
    }
  ],
  "serviceAddresses": {
    "compositeAddress": "pop-os/127.0.1.1:7000",
    "productAddress": "pop-os/127.0.1.1:7001",
    "reviewAddress": "pop-os/127.0.1.1:7003",
    "recommendationAddress": ""
  }
}
```

#### 3. Verify that there aren't any reviews for productId 213
```shell
http http://localhost:7000/product-composite/213 --unsorted;
```

#### Output
```json
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 409

{
    "productId": 213,
    "name": "name-213",
    "weight": 123,
    "recommendations": [
        {
            "recommendationId": 1,
            "author": "Author 1",
            "rate": 1
        },
        {
            "recommendationId": 2,
            "author": "Author 2",
            "rate": 2
        },
        {
            "recommendationId": 3,
            "author": "Author 3",
            "rate": 3
        }
    ],
    "reviews": [],
    "serviceAddresses": {
        "compositeAddress": "pop-os/127.0.1.1:7000",
        "productAddress": "pop-os/127.0.1.1:7001",
        "reviewAddress": "",
        "recommendationAddress": "pop-os/127.0.1.1:7002"
    }
}
```

---

### Verify that a 422 (Unprocessable Entity) for a productId that is out of range (-1) 

```shell
http http://localhost:7000/product-composite/-1 --unsorted
```

#### Output
```shell
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json
Content-Length: 159

{
    "timestamp": "2024-12-23T00:39:46.01206139-05:00",
    "path": "/product-composite/-1",
    "message": "Invalid productId: -1",
    "status": 422,
    "error": "Unprocessable Entity"
}
```
---

### Verify that a 400 (Bad Request) for a productId that isn't a number (invalid format)

```shell
http http://localhost:7000/product-composite/invalidProductId --unsorted
```

#### Output
(I truncated the stack trace. You're going to see the whole ugly thing when you execute it...)
```shell
(base) ~/IdeaProjects/ServiceTransformation git:[main]
http http://localhost:7000/product-composite/invalidProductId --unsorted
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 9107

{
    "timestamp": "2024-12-23T05:43:05.505+00:00",
    "path": "/product-composite/invalidProductId",
    "status": 400,
    "error": "Bad Request",
    "requestId": "7c94c632-36",
    "message": "Type mismatch.",
    "trace": "<Java Stack Trace Truncated>"
}
```

---

---

## Automated Tests (Test Runner)

With the microservices started, execute the following command

```shell
./testRunner.sh
```

#### Expected Output

```shell
(base) ~/IdeaProjects/ServiceTransformation git:[main]
./testRunner.sh
HOST=localhost
PORT=7000
Test OK (HTTP Status: 200)
Test OK (actual value: 1)
Test OK (actual value: 3)
Test OK (actual value: 3)
Test OK (HTTP Status: 404, {"timestamp":"2024-12-23T09:11:23.9658777-05:00","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2024-12-23T09:11:24.130368707-05:00","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2024-12-23T14:11:24.162+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"b4fbfffe-6","message":"Type mismatch.","trace":"org.springframework.beans.TypeMismatchException: Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"invalidProductId\"\n\tat org.springframework.beans.TypeConverterSupport.convertIfNecessary(TypeConverterSupport.java:87)\n\tat org.springframework.beans.TypeConverterSupport.convertIfNecessary(TypeConverterSupport.java:53)\n\tat org.springframework.validation.DataBinder.convertIfNecessary(DataBinder.java:866)\n\tat org.springframework.web.reactive.result.method.annotation.AbstractNamedValueArgumentResolver.applyConversion(AbstractNamedValueArgumentResolver.java:209)\n\tat org.springframework.web.reactive.result.method.annotation.AbstractNamedValueArgumentResolver.lambda$resolveArgument$0(AbstractNamedValueArgumentResolver.java:117)\n\tat reactor.core.publisher.FluxFlatMap.trySubscribeScalarMap(FluxFlatMap.java:153)\n\tat reactor.core.publisher.MonoFlatMap.subscribeOrReturn(MonoFlatMap.java:53)\n\tat reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:63)\n\tat reactor.core.publisher.MonoZip$ZipCoordinator.request(MonoZip.java:220)\n\tat reactor.core.publisher.MonoFlatMap$FlatMapMain.request(MonoFlatMap.java:194)\n\tat reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.onSubscribe(MonoIgnoreThen.java:135)\n\tat reactor.core.publisher.MonoFlatMap$FlatMapMain.onSubscribe(MonoFlatMap.java:117)\n\tat reactor.core.publisher.MonoZip.subscribe(MonoZip.java:129)\n\tat reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:76)\n\tat reactor.core.publisher.MonoDefer.subscribe(MonoDefer.java:53)\n\tat reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.subscribeNext(MonoIgnoreThen.java:241)\n\tat reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.onComplete(MonoIgnoreThen.java:204)\n\tat reactor.core.publisher.MonoFlatMap$FlatMapMain.onComplete(MonoFlatMap.java:189)\n\tat reactor.core.publisher.Operators.complete(Operators.java:137)\n\tat reactor.core.publisher.MonoZip.subscribe(MonoZip.java:121)\n\tat reactor.core.publisher.Mono.subscribe(Mono.java:4576)\n\tat reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.subscribeNext(MonoIgnoreThen.java:265)\n\tat reactor.core.publisher.MonoIgnoreThen.subscribe(MonoIgnoreThen.java:51)\n\tat reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:76)\n\tat reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:165)\n\tat reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79)\n\tat reactor.core.publisher.FluxSwitchIfEmpty$SwitchIfEmptySubscriber.onNext(FluxSwitchIfEmpty.java:74)\n\tat reactor.core.publisher.MonoNext$NextSubscriber.onNext(MonoNext.java:82)\n\tat reactor.core.publisher.FluxConcatMapNoPrefetch$FluxConcatMapNoPrefetchSubscriber.innerNext(FluxConcatMapNoPrefetch.java:259)\n\tat reactor.core.publisher.FluxConcatMap$ConcatMapInner.onNext(FluxConcatMap.java:865)\n\tat reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:129)\n\tat reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.onNext(MonoPeekTerminal.java:180)\n\tat reactor.core.publisher.Operators$ScalarSubscription.request(Operators.java:2571)\n\tat reactor.core.publisher.MonoPeekTerminal$MonoTerminalPeekSubscriber.request(MonoPeekTerminal.java:139)\n\tat reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.request(FluxMapFuseable.java:171)\n\tat reactor.core.publisher.Operators$MultiSubscriptionSubscriber.request(Operators.java:2331)\n\tat reactor.core.publisher.FluxConcatMapNoPrefetch$FluxConcatMapNoPrefetchSubscriber.request(FluxConcatMapNoPrefetch.java:339)\n\tat reactor.core.publisher.MonoNext$NextSubscriber.request(MonoNext.java:108)\n\tat reactor.core.publisher.Operators$MultiSubscriptionSubscriber.set(Operators.java:2367)\n\tat reactor.core.publisher.Operators$MultiSubscriptionSubscriber.onSubscribe(Operators.java:2241)\n\tat reactor.core.publisher.MonoNext$NextSubscriber.onSubscribe(MonoNext.java:70)\n\tat reactor.core.publisher.FluxConcatMapNoPrefetch$FluxConcatMapNoPrefetchSubscriber.onSubscribe(FluxConcatMapNoPrefetch.java:164)\n\tat reactor.core.publisher.FluxIterable.subscribe(FluxIterable.java:201)\n\tat reactor.core.publisher.FluxIterable.subscribe(FluxIterable.java:83)\n\tat reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:76)\n\tat reactor.core.publisher.MonoDefer.subscribe(MonoDefer.java:53)\n\tat reactor.core.publisher.Mono.subscribe(Mono.java:4576)\n\tat reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.subscribeNext(MonoIgnoreThen.java:265)\n\tat reactor.core.publisher.MonoIgnoreThen.subscribe(MonoIgnoreThen.java:51)\n\tat reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:76)\n\tat reactor.core.publisher.MonoDeferContextual.subscribe(MonoDeferContextual.java:55)\n\tat reactor.netty.http.server.HttpServer$HttpServerHandle.onStateChange(HttpServer.java:1211)\n\tat reactor.netty.ReactorNetty$CompositeConnectionObserver.onStateChange(ReactorNetty.java:716)\n\tat reactor.netty.transport.ServerTransport$ChildObserver.onStateChange(ServerTransport.java:486)\n\tat reactor.netty.http.server.HttpServerOperations.handleDefaultHttpRequest(HttpServerOperations.java:843)\n\tat reactor.netty.http.server.HttpServerOperations.onInboundNext(HttpServerOperations.java:776)\n\tat reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:115)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:444)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)\n\tat io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:412)\n\tat reactor.netty.http.server.HttpTrafficHandler.channelRead(HttpTrafficHandler.java:267)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:442)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)\n\tat io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:412)\n\tat io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436)\n\tat io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:346)\n\tat io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:318)\n\tat io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:442)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)\n\tat io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:412)\n\tat io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1357)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:440)\n\tat io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:420)\n\tat io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:868)\n\tat io.netty.channel.epoll.AbstractEpollStreamChannel$EpollStreamUnsafe.epollInReady(AbstractEpollStreamChannel.java:799)\n\tat io.netty.channel.epoll.EpollEventLoop.processReady(EpollEventLoop.java:501)\n\tat io.netty.channel.epoll.EpollEventLoop.run(EpollEventLoop.java:399)\n\tat io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)\n\tat io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n\tat io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n\tat java.base/java.lang.Thread.run(Thread.java:833)\nCaused by: java.lang.NumberFormatException: For input string: \"invalidProductId\"\n\tat java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:67)\n\tat java.base/java.lang.Integer.parseInt(Integer.java:668)\n\tat java.base/java.lang.Integer.valueOf(Integer.java:999)\n\tat org.springframework.util.NumberUtils.parseNumber(NumberUtils.java:201)\n\tat org.springframework.beans.propertyeditors.CustomNumberEditor.setAsText(CustomNumberEditor.java:115)\n\tat org.springframework.beans.TypeConverterDelegate.doConvertTextValue(TypeConverterDelegate.java:439)\n\tat org.springframework.beans.TypeConverterDelegate.doConvertValue(TypeConverterDelegate.java:412)\n\tat org.springframework.beans.TypeConverterDelegate.convertIfNecessary(TypeConverterDelegate.java:161)\n\tat org.springframework.beans.TypeConverterSupport.convertIfNecessary(TypeConverterSupport.java:80)\n\t... 81 more\n"})
Test OK (actual value: "Type mismatch.")
End, all tests OK:  Mon Dec 23 09:11:24 AM EST 2024

```