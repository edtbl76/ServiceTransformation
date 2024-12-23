# Testing the System

## Contents
- [Automated Tests (Local) w/ testRunner.sh](#automated-tests-local)
- [Manual Tests (Docker Endpoint) w/ httpie](#manually-testing-docker)
- [Manual Tests (Local) w/ curl, httpie](#manual-testing-local)

## Documentation
- [Readme](../README.md)
- [Version Information](VERSION.md)
- [Release Notes](RELEASE.md)
- [Building](BUILD.md)
- [Running Services](RUNNING.md)


---

## Automated Tests (Local)
With the microservices started **locally**, execute the following command

```shell
PORT=7000 ./testRunner.sh
```
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

---

---

## Manually Testing (Docker)
```shell
http http://localhost:8080/product/3
```

```text
HTTP/1.1 200 OK
Content-Length: 92
Content-Type: application/json

{
    "name": "name-3",
    "productId": 3,
    "serviceAddress": "312778e9cf91/172.17.0.2:8080",
    "weight": 123
}
```

---

---
## Manual Testing (Local)

### Confirm you can retrieve a record (curl)

```shell
curl http://localhost:7000/product-composite/1 -s | jq
```
```text
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
```text
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

 1. Verify that a 404 (Not Found) is returned for a non existing productId
```shell
http http://localhost:7000/product-composite/13 --unsorted;
```
```text
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

2. Verify that there aren't any recommendations for productId 113
```shell
http http://localhost:7000/product-composite/113 --unsorted;
```
```text
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
 3. Verify that there aren't any reviews for productId 213
```shell
http http://localhost:7000/product-composite/213 --unsorted;
```
```text
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
