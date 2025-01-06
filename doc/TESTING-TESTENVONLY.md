# Testing in the Test Environment Only

These examples do **not** use seeded data. 

## Contents

- [Testing messaging (RabbitMQ)](#testing-messaging-rabbitmq)
- [Testing streamed-partitions (RabbitMQ)](#testing-streamed-partitions-rabbitmq)
- [Testing streamed-partitions (Kafka)](#testing-streamed-partitions-kafka)
- [Testing Spring Cloud Gateway](#testing-spring-cloud-gateway)
- [Testing Spring OAuth2 Server](#testing-spring-oauth2-server)
- [Testing Authorization (OpenAPI)]()
- [Testing Config Server](#testing-config-server)
- [Testing Encrypt/Decrypt Endpoints](#testing-encryptdecrypt-endpoints)

---

## Helpers
- [Setting up the environment](#setting-up-the-environment)
- [Shutting down the environment](#shutting-down-the-environment)


---
## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)

---

## Setting up the environment

- [1. Build microservices](#1-build-microservices)
- [2. Build Containers](#2-build--start-containers)
  - [Default (docker-compose.yml)](#default-docker-composeyml)
  - [Streamed-partitioning (RabbitMQ)](#streamed-partitioning-rabbitmq)
  - [Streamed-partitioning (Kafka)](#streamed-partitioning-kafka)
- [4. Validate Microservices are up](#3-validate-microservices-are-up)

### 1. Build Microservices

```shell
./gradlew build
```

### 2. Build & Start Containers

#### Default (docker-compose.yml)

```shell
docker-compose build
docker-compose up -d
```

#### Streamed-Partitioning (RabbitMQ)

```shell
export COMPOSE_FILE=docker-compose-partitions.yml 
docker-compose build
docker-compose up -d
```
You will note that this uses 2 instances of the product, recommendations, and reviews microservices.
```text
[+] Running 11/11
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.0s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                         6.4s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                         6.8s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                        31.4s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                         6.7s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                        31.2s 
 ✔ Container servicetransformation-review-p2-1          Started                                                                                                                                                                                                                                        31.2s 
 ✔ Container servicetransformation-product-p2-1         Started                                                                                                                                                                                                                                         6.9s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                         7.0s 
 ✔ Container servicetransformation-recommendation-p2-1  Started                                                                                                                                                                                                                                         6.8s 
 ✔ Container servicetransformation-recommendation-1     Started   
```


#### Streamed Partitioning (Kafka)

```shell
export COMPOSE_FILE=docker-compose-kafka.yml 
docker-compose build
docker-compose up -d
```

NOTE: 
rabbitmq is replaced by kafka and zookeeper. Similar to the streamed partitions, you will see 2 each of the 
product, recommendations and review microservices. 
```text
[+] Running 10/12
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.0s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                         8.2s 
 ✔ Container servicetransformation-zookeeper-1          Started                                                                                                                                                                                                                                         2.6s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                        33.2s 
 ✔ Container servicetransformation-kafka-1              Started                                                                                                                                                                                                                                         1.4s 
 ✔ Container servicetransformation-recommendation-p2-1  Started                                                                                                                                                                                                                                         7.1s 
 ⠸ Container servicetransformation-review-1             Starting                                                                                                                                                                                                                                       31.3s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                         1.6s 
 ⠸ Container servicetransformation-review-p2-1          Starting                                                                                                                                                                                                                                       31.3s 
 ✔ Container servicetransformation-product-p2-1         Started                                                                                                                                                                                                                                         6.8s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                         6.9s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                         7.0s 
```


---
### 3. Validate Microservices are up

```shell
curl -s localhost:8080/actuator/health | jq -r .status
```
```text
UP
```
If you've just started up the environment, the result might be `DOWN`. It shouldn't take longer than a minute or so to start up. 

---

## Shutting down the environment

- Sets the docker-compose ENV back to default
- Shuts down landscape and cleans up any orphaned containers. 

```shell
unset COMPOSE_FILE
docker-compose down --remove-orphans
```
---

---
## Testing Messaging (RabbitMQ)

Make sure to [set up your environment](#setting-up-the-environment).

- [1. Create a composite to test reactive streams / event-driven architecture](#1-create-composite)
- [2. Inspect RabbitMQ's administrative page](#2-inspect-rabbit)
- [3. Inspect products.auditGroup](#3-inspect-productsauditgroup)
- [4. Curl GET to validate message](#4-get-the-message-via-curl)
- [5. Delete the record and validate](#5-delete-the-record)
- [6. Inspecting RabbitMQ after deletions](#6-inspecting-rabbit-after-deletions)

### 1. Create Composite

- Creating the composite product w/ Cloud Stream and RabbitMQ will create one RabbitMQ `exchange` per `topic` and a set of queues. (Depends on the config)

```shell
BODY='{"productId":1,"name":"product name C","weight":300,"recommendations":[
{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
curl -X POST localhost:8080/product-composite -H "Content-Type:application/json" --data "$BODY"
```


### 2. Inspect Rabbit

1. Go to the [RabbitMQ Admin Portal](http://localhost:15672/#) (guest/guest).  
2. Navigate to the `Queues and Streams` tab.

![rabbit-queues-001.png](img/rabbit-queues-001.png)

```text
What is the auditGroup??
In Rabbit, messages are removed from the actual service queues after consumers have processed them. The auditGroups
serve as a record of the messages that were processed historically. 
```

For each topic there are 3 queues
- `<service>`.auditGroup
- `<service>`.`<service>`Group
- `<service>`.`<service>`Group.dlq (Dead Letter Queue)

You should see that each of the `auditGroup` queues contain messages. 
- `products.auditGroup`: 1
- `recommendations.auditGroup`: 3
- `reviews.auditGroup`: 3
---

### 3. Inspect products.auditGroup

1. click on `products.auditGroup`
2. scroll down to get messages
3. click the `Get Message(s)` button

![products-auditGroup-get-messages.png](img/products-auditGroup-get-messages.png)
- note the properties (message_id, priority, delivery_mode)
- note the headers
  - the partitionKey is important when using `partitioning`
- compare the payload to the message we sent (It should be the same)

### 4. Get the message via curl

```shell
curl localhost:8080/product-composite/1 -s | jq
```
```text

{
  "productId": 1,
  "name": "product name C",
  "weight": 300,
  "recommendations": [
    {
      "recommendationId": 1,
      "author": "author 1",
      "rate": 1,
      "content": "content 1"
    },
    {
      "recommendationId": 2,
      "author": "author 2",
      "rate": 2,
      "content": "content 2"
    },
    {
      "recommendationId": 3,
      "author": "author 3",
      "rate": 3,
      "content": "content 3"
    }
  ],
  "reviews": [
    {
      "reviewId": 1,
      "author": "author 1",
      "subject": "subject 1",
      "content": "content 1"
    },
    {
      "reviewId": 2,
      "author": "author 2",
      "subject": "subject 2",
      "content": "content 2"
    },
    {
      "reviewId": 3,
      "author": "author 3",
      "subject": "subject 3",
      "content": "content 3"
    }
  ],
  "serviceAddress": {
    "compositeAddress": "af20d18bf452/192.168.16.5:8080",
    "productAddress": "9d97fd5cf379/192.168.16.7:8080",
    "recommendationAddress": "414094ca1cf4/192.168.16.8:8080",
    "reviewAddress": "c5316c0ed7b6/192.168.16.6:8080"
  }
}
```

From this we can see that Rabbit delivered the records appropriately, and we can query the microservices

### 5. Delete the record

Command
```shell
curl -X DELETE localhost:8080/product-composite/1
```

You won't get a response. Try running the get again to validate the deletion.
```shell
curl localhost:8080/product-composite/1 -s | jq
```

```text
{
  "timestamp": "2025-01-02T04:01:20.496718852Z",
  "path": "/product-composite/1",
  "message": "No product found for productId: 1",
  "status": 404,
  "error": "Not Found"
}
```

### 6. Inspecting Rabbit (After Deletions)

- Log back in to RabbitMQ and inspect the queues to see the delete messages.
  - (When clicking get messages, it defaults to selecting only a single message,you can type any number you want there to see the rest of the messages)
- (play around!)
- Remember to [clean up after you are done](#shutting-down-the-environment).


---

## Testing Streamed Partitions (RabbitMQ)

When [setting up your environment](#setting-up-the-environment), select the [streamed-partitions (RabbitMQ)](#streamed-partitioning-rabbitmq) option for launching the containers. 



- [1. Create 2 composites to test streamed partitions](#1-create-composites-streamed-partitions)
- [2. Inspect Rabbit (Streamed Partitions)](#2-inspect-rabbit-partitions)
- [3. Inspect auditGroups](#3-inspecting-the-auditqueues)

---

### 1. Create Composites (Streamed Partitions)

- Creating the composite product w/ Cloud Stream and RabbitMQ will create one RabbitMQ `exchange` per `topic` and a set of queues. (Depends on the config)

```shell
BODY1='{"productId":1,"name":"product name C","weight":300,"recommendations":[
{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
curl -X POST localhost:8080/product-composite -H "Content-Type:application/json" --data "$BODY1"

BODY2='{"productId":2,"name":"product name D","weight":300,"recommendations":[
{"recommendationId":4,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":5,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":6,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":4,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":5,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":6,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
curl -X POST localhost:8080/product-composite -H "Content-Type:application/json" --data "$BODY2"
```

---
### 2. Inspect Rabbit (Partitions)

1. Go to the [RabbitMQ Admin Portal](http://localhost:15672/#) (guest/guest).
2. Navigate to the `Queues and Streams` tab.

![rabbit-queues-partitions.png](img/rabbit-queues-partitions.png)

This should look a bit different
For each topic there are 5 queues
- 2 `<service>`.auditGroups (1 for each partition)
- 2 `<service>`.`<service>`Groups (1 for each partition) 
- 1 `<service>`.`<service>`Group.dlq (Dead Letter Queue)

You should see that each of the `auditGroup` queues contain messages.
- `products.auditGroup`: 1 per partition 
- `recommendations.auditGroup`: 3 per partition
- `reviews.auditGroup`: 3 per partition
---

### 3. Inspecting the auditQueues

If you inspect any of the auditQueues, you'll note that the contents are split. 
- The messages we sent w/ **productId 1** are associated with **partitionKey 1**
  - products.auditGroup-0
  - recommendations.auditGroup-1
  - reviews.auditGroup-1
- The messages we sent w/ **productId 2** are associated with **partitionKey 2**
  - products.auditGroup-1
  - recommendations.auditGroup-0
  - reviews.auditGroup-0


- Feel Free to repeat some of the previous tests.

- Remember to [clean up when you are done](#shutting-down-the-environment).

---


## Testing Streamed Partitions (Kafka)

When [setting up your environment](#setting-up-the-environment), select the [streamed-partitions (Kafka)](#streamed-partitioning-kafka) option for launching the containers.

- [1. Get list of topics](#1-get-list-of-topics)
- [2. Create composites (Kafka)](#2-create-composites-kafka)
- [3. Inspecting topics](#3-inspecting-topics)
- [4. Inspecting messages in a topic](#4-inspecting-messages-in-topics)
- [5. Inspecting messages in a specific partition](#5-inspecting-messages-in-a-specific-partition)


### 1. Get list of topics

```shell
docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```
```text
__consumer_offsets
error.products.productsGroup
error.recommendations.recommendationsGroup
error.reviews.reviewsGroup
products
recommendations
reviews
```

Each microservice only has 2 queues (unlike the 3 we had in RabbitMQ):
- queues prefixed w/ `error.` are dead letter queues
- the event queues are those w/ the names of the microservice: `products, recommendations, reviews`.

Kafka retains events in the topics even after consumers have processed them, so there aren't any auditGroups.

---

## 2. Create Composites (Kafka)


```shell
BODY1='{"productId":1,"name":"product name C","weight":300,"recommendations":[
{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
curl -X POST localhost:8080/product-composite -H "Content-Type:application/json" --data "$BODY1"

BODY2='{"productId":2,"name":"product name D","weight":300,"recommendations":[
{"recommendationId":4,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":5,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":6,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":4,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":5,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":6,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
curl -X POST localhost:8080/product-composite -H "Content-Type:application/json" --data "$BODY2"
```


### 3. Inspecting topics

```shell
docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic products
```
```text
Topic: products TopicId: zb3e0G-2Q06elXMpTQQHkg PartitionCount: 2       ReplicationFactor: 1    Configs: 
        Topic: products Partition: 0    Leader: 1001    Replicas: 1001  Isr: 1001       Elr: N/A        LastKnownElr: N/A
        Topic: products Partition: 1    Leader: 1001    Replicas: 1001  Isr: 1001       Elr: N/A        LastKnownElr: N/A
```


### 4. Inspecting messages in topics

```shell
docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic products --from-beginning --timeout-ms 10000
```
```text
{"eventType":"CREATE","key":2,"data":{"productId":2,"name":"product name D","weight":300,"serviceAddress":null},"eventCreatedAt":"2025-01-02T05:29:09.681559746Z[UTC]"}
{"eventType":"CREATE","key":1,"data":{"productId":1,"name":"product name C","weight":300,"serviceAddress":null},"eventCreatedAt":"2025-01-02T05:29:09.140813853Z[UTC]"}
[2025-01-02 05:30:40,895] ERROR Error processing message, terminating consumer process:  (org.apache.kafka.tools.consumer.ConsoleConsumer)
org.apache.kafka.common.errors.TimeoutException
Processed a total of 2 messages
```
Ignore the error, you told it to do that! (That's the timeout.) It should get the messages you sent in at least 10
seconds, if not, try extending the timeout or removing it altogether. (If you remove the timeout, you have to hit CTRL-C to
exit the command)


### 5. Inspecting messages in a specific partition

```shell
docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic products --from-beginning --timeout-ms 10000 --partition 1
```
```text
{"eventType":"CREATE","key":1,"data":{"productId":1,"name":"product name C","weight":300,"serviceAddress":null},"eventCreatedAt":"2025-01-02T05:29:09.140813853Z[UTC]"}
[2025-01-02 05:32:54,795] ERROR Error processing message, terminating consumer process:  (org.apache.kafka.tools.consumer.ConsoleConsumer)
org.apache.kafka.common.errors.TimeoutException
Processed a total of 1 messages
```

[Remember to turn everything off when you are done playing around!](#shutting-down-the-environment)

---

## Testing Spring Cloud Gateway

- [1. Validating the gateway URL](#1-validating-the-gateway-url)
- [2. Validating routes](#2-validating-routes)
- [3. Getting List of Routes](#3-getting-list-of-routes-)


### 1. Validating the gateway URL

```shell
http --verify=no https://localhost:8443/actuator/gateway
```

```shell
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --verify=no https://localhost:8443/actuator/gateway
HTTP/1.1 200 OK
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 2522
Content-Type: application/json
Expires: 0
Pragma: no-cache
Referrer-Policy: no-referrer
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0

[
    {
        "href": "/actuator/gateway/routedefinitions",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/globalfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routefilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-server/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-login/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-error/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite-swagger-ui/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite-swagger-ui-webjars/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-api/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-web-start/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-web-other/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_200/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_418/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_501/combinedfilters",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes",
        "methods": [
            "POST",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-server",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-login",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/oauth2-error",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite-swagger-ui",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/product-composite-swagger-ui-webjars",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-api",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-web-start",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/eureka-web-other",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_200",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_418",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routes/host_route_501",
        "methods": [
            "POST",
            "DELETE",
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/routepredicates",
        "methods": [
            "GET"
        ]
    },
    {
        "href": "/actuator/gateway/refresh",
        "methods": [
            "POST"
        ]
    }
]
```
---

### 2. Validating Routes

```shell
http --verify=no https://localhost:8443/actuator/gateway/routes
```

```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --verify=no https://localhost:8443/actuator/gateway/routes
HTTP/1.1 200 OK
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Type: application/json
Expires: 0
Pragma: no-cache
Referrer-Policy: no-referrer
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0
transfer-encoding: chunked

[
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/product-composite/**], match trailing slash: true",
        "route_id": "product-composite",
        "uri": "lb://product-composite"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/oauth2/**], match trailing slash: true",
        "route_id": "oauth2-server",
        "uri": "lb://auth-server"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/login/**], match trailing slash: true",
        "route_id": "oauth2-login",
        "uri": "lb://auth-server"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/error/**], match trailing slash: true",
        "route_id": "oauth2-error",
        "uri": "lb://auth-server"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/openapi/**], match trailing slash: true",
        "route_id": "product-composite-swagger-ui",
        "uri": "lb://product-composite"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/webjars/**], match trailing slash: true",
        "route_id": "product-composite-swagger-ui-webjars",
        "uri": "lb://product-composite"
    },
    {
        "filters": [
            "[[SetPath template = '/eureka/{segment}'], order = 1]"
        ],
        "order": 0,
        "predicate": "Paths: [/eureka/api/{segment}], match trailing slash: true",
        "route_id": "eureka-api",
        "uri": "http://eureka:8761"
    },
    {
        "filters": [
            "[[SetPath template = '/'], order = 1]"
        ],
        "order": 0,
        "predicate": "Paths: [/eureka/web], match trailing slash: true",
        "route_id": "eureka-web-start",
        "uri": "http://eureka:8761"
    },
    {
        "filters": [],
        "order": 0,
        "predicate": "Paths: [/eureka/**], match trailing slash: true",
        "route_id": "eureka-web-other",
        "uri": "http://eureka:8761"
    },
    {
        "filters": [
            "[[SetPath template = '/200'], order = 1]"
        ],
        "order": 0,
        "predicate": "(Hosts: [i.feel.lucky:8080] && Paths: [/headerrouting/**], match trailing slash: true)",
        "route_id": "host_route_200",
        "uri": "http://httpstat.us:80"
    },
    {
        "filters": [
            "[[SetPath template = '/418'], order = 1]"
        ],
        "order": 0,
        "predicate": "(Hosts: [im.a.teapot:8080] && Paths: [/headerrouting/**], match trailing slash: true)",
        "route_id": "host_route_418",
        "uri": "http://httpstat.us:80"
    },
    {
        "filters": [
            "[[SetPath template = '/501'], order = 1]"
        ],
        "order": 0,
        "predicate": "Paths: [/headerrouting/**], match trailing slash: true",
        "route_id": "host_route_501",
        "uri": "http://httpstat.us:80"
    }
]
```
### 3. Getting List of Routes 

```shell
http --verify=no https://localhost:8443/actuator/gateway/routes | jq '.[] | {"\(.route_id)":"\(.uri)"}' | grep -v '{\|}'
```

```text
  "product-composite": "lb://product-composite"
  "oauth2-server": "lb://auth-server"
  "oauth2-login": "lb://auth-server"
  "oauth2-error": "lb://auth-server"
  "product-composite-swagger-ui": "lb://product-composite"
  "product-composite-swagger-ui-webjars": "lb://product-composite"
  "eureka-api": "http://eureka:8761"
  "eureka-web-start": "http://eureka:8761"
  "eureka-web-other": "http://eureka:8761"
  "host_route_200": "http://httpstat.us:80"
  "host_route_418": "http://httpstat.us:80"
  "host_route_501": "http://httpstat.us:80"
```

---

## Testing Spring OAuth2 Server

- [Getting write access token (AuthZ code grant flow)](#1-getting-read-access-tokens-using-authz-code-grant-flow)
- [Getting read access token (AuthZ code grant flow)](#2-getting-write-access-token-using-authz-code-grant-flow)


### 1. Getting read access tokens using authz code grant flow

1. Login [https://localhost:8443/oauth2/authorize](https://localhost:8443/oauth2/authorize)

```text
Credentials
user: username
pass: password
```

2. Visit the following link [https://localhost:8443/oauth2/authorize?response_type=code&client_id=reader&redirect_uri=https://my.redirect.uri&scope=product:read&state=35725](https://localhost:8443/oauth2/authorize?response_type=code&client_id=reader&redirect.uri=https://my.redirect.uri&scope=product:read&state=35725) 



![your-connection-is-not-private.png](img/your-connection-is-not-private.png)If you encounter this page, click "Advanced"  and then "proceed to localhost (unsafe)". 


3. When you get to this page, select the check box and click "Submit Consent"

![authz-code-grant-flow-consent.png](img/authz-code-grant-flow-consent.png)


4. DON'T PANIC

You're going to encounter some form of error page

I've run into 2...
- DNS_PROBE_FINISHED_NXDOMAIN
- ERR_NAME_NOT_RESOLVED

Look carefully at the urlbar, and you'll see that the request redirected you to `https://my.direct.uri` and provided you the codegrant.

Example: 
```text
https://my.redirect.uri/?code=wpgtv6LQtrisltDc08jtHO0GdMjybOD5p7HZ7dZXb6K_D7afGDV5cvCQ7SaXBC5MQFdyyLqmgrAWIbD0gGRoZWtqwk4YZf9VNqCHQ5yPhO2K15QXSrh_Kk7xRYyTKQlX&state=35725
```

4. Get Refresh Token

We're going to emulate the exchange between the authz code w/ an access token using a curl command
(replace my code w/ yours)

```shell
curl -k https://reader:reader@localhost:8443/oauth2/token -d grant_type=authorization_code -d client_id=reader -d redirect_uri=https://my.redirect.uri -d code=h0r0ZPBoHivMPI4nbHw8vv76zPQAzgF-dqUcrke_kSOFOvZixu7ZTVZLNSV7VjfMk_4_nWGbhCPPYjnk1Bnxtn4prCPWb39OGpignZSZG2zOQjj0O2yPvL_GeOO9C8mu -s | jq
```

```text
{
  "access_token": "eyJraWQiOiI5ZTY4MmNhMS04MmUyLTQzYjAtOWY0Yy1hMjQyZDU4YjE3ZGIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImF1ZCI6InJlYWRlciIsIm5iZiI6MTczNjExMzgyNywic2NvcGUiOlsicHJvZHVjdDpyZWFkIl0sImlzcyI6Imh0dHA6Ly9hdXRoLXNlcnZlcjo5OTk5IiwiZXhwIjoxNzM2MTE3NDI3LCJpYXQiOjE3MzYxMTM4Mjd9.N3cNDxu7RUmWhbdMKllkLbn0nlkc_2hTP9HLKqMWLFOqRAw0NmCyZeC4fbwVKpYJQ0M7vbAMMhcC-nW9O-r9NzuWfcYMljeorRe_Oi6HPR8cJiQIgy1y7CldydQDQ5r-3kOy8x8oYUwKnGjxCSDaSHlJboxXED406M75zZ7V9HWsiY__u_4bcOiq18QsRiMwnnELip4bnYTk1mPi-xFaFwbk7lgnPJUXSOsBuxVttTriWEezhUiwcq4pwfFrik3qcOZCSjMW_sB-DeLjcWLyVhMQINSijyuU1dl13KHD7a6EVOCAzEMVdVmkOFUwWxxIEdXGL4b6V9t8Oc4fq8nSEg",
  "refresh_token": "Q3-7bhighp7ys5MIdMZWUZaDQkKONhUizn2iv9qzWENSJRVZlQq7Ye5cyUofIqBh476C83jgVcOrG9UgqCYy5peU2Tw0NXupJa4yekPz1uWWJXNBnNN4SGW_t_7iTnGt",
  "scope": "product:read",
  "token_type": "Bearer",
  "expires_in": 3599
}
```


### 2. Getting Write access token using authz code grant flow


0. (Delete any cookies)

1. Login [https://localhost:8443/oauth2/authorize](https://localhost:8443/oauth2/authorize)

```text
Credentials
user: username
pass: password
```

2. [https://localhost:8443/oauth2/authorize?response_type=code&client_id=writer&redirect_uri=https://my.redirect.uri&scope=product:read+product:write&state=72489](https://localhost:8443/oauth2/authorize?response_type=code&client_id=writer&redirect.uri=https://my.redirect.uri&scope=product:read+product:write&state=72489)


2. When you get to this page, select the check box and click "Submit Consent"

For the writer, it will look different (both read and write consent checkboxes will be present)
   ![authz-code-grant-flow-consent.png](img/authz-code-grant-flow-consent.png)

3. DON'T PANIC

You're going to encounter some form of error page

I've run into 2...
- DNS_PROBE_FINISHED_NXDOMAIN
- ERR_NAME_NOT_RESOLVED

Look carefully at the urlbar, and you'll see that the request redirected you to `https://my.direct.uri` and provided you the codegrant.

Example:
```text
https://my.redirect.uri/?code=r3-yMUrN4M7MukgQe3CfiWKOPvRVMZCbTFa0MtX-JibepyqSxSIkOtTTkydouOjhpittfq9neenulRkm2chSZOgpk2ETcK3lIVlF7LLYDl_QvfRzuMlRLAWL9_fxK0T6&state=72489
```

4. Get Refresh Token

We're going to emulate the exchange between the authz code w/ an access token using a curl command
(replace my code w/ yours)

```shell
curl -k https://writer:writer@localhost:8443/oauth2/token -d grant_type=authorization_code -d client_id=writer -d redirect_uri=https://my.redirect.uri -d code=r3-yMUrN4M7MukgQe3CfiWKOPvRVMZCbTFa0MtX-JibepyqSxSIkOtTTkydouOjhpittfq9neenulRkm2chSZOgpk2ETcK3lIVlF7LLYDl_QvfRzuMlRLAWL9_fxK0T6 -s | jq
```

```text
{
  "access_token": "eyJraWQiOiI5ZTY4MmNhMS04MmUyLTQzYjAtOWY0Yy1hMjQyZDU4YjE3ZGIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImF1ZCI6IndyaXRlciIsIm5iZiI6MTczNjExNDU4NCwic2NvcGUiOlsicHJvZHVjdDp3cml0ZSIsInByb2R1Y3Q6cmVhZCJdLCJpc3MiOiJodHRwOi8vYXV0aC1zZXJ2ZXI6OTk5OSIsImV4cCI6MTczNjExODE4NCwiaWF0IjoxNzM2MTE0NTg0fQ.UeRlyT1tFxjzjOw-ZBcjfNJvi0CcNthJZBB8MT5BUQ3wGiAtF1TkthApqqRYs5dwVu0pIEa5vc0lW12CmvjONxPR7RS7JGrlCm7nFqdytkifuEbLyephQzNrdmIjA0K3mhztfwWKt0PkVCZnm3aN6jXPhIQNRdFw8_YsWRrgLbObPdjVdED8TRNeaN-Ri9Gq1UTORWfaoIjQ0tP2XzXiHtYxmK4GC1W8OntxeACGEa_Aq6_sbUiZ1dDi9tI0yac-iSKf6qucaIeYiEHo8vS9yRS-u7F_XYeJr4r61YOx2D4yq6PJbyd8Pz_nLH5GLdQCTLZBqHF7NJMr8OdNh4VLCQ",
  "refresh_token": "AMNXF0jkVI8i2bpTZroEDiP00DfCVpeDEM8ECbhMAej0s3l4BLfeesn2xwOH_AAfAqphFK6P52cVGSZ_RVuOw_JuNf7Uk3TR3x_3asJtVPLqCqepZ8LvogjS4AkahtZ1",
  "scope": "product:write product:read",
  "token_type": "Bearer",
  "expires_in": 3599
}
```
---

## Testing Authorization Flow (OpenAPI)

1. Visit the [Swagger/OpenAPI UI](https://localhost:8443/openapi/webjars/swagger-ui/index.html)

![openapi-ui-authorize.png](img/openapi-ui-authorize.png)

2. Select the "Authorize" button

![openapi-available-authorizations.png](img/openapi-available-authorizations.png)

3. select the `client_id`, `client_secret` pair and the scopes and click the "Authorize" button

(We've only coded 2: reader/reader and writer/writer)

You may be prompted to login and submit consent. 

![openapi-available-authorizations-result.png](img/openapi-available-authorizations-result.png)


---

## Testing Config Server


```shell
## curl
curl -H "accept:application/json" -k https://username:password@localhost:8443/config/product/docker -s | jq
## httpie
http --unsorted --verify=no --auth username:password https://localhost:8443/config/product/docker Accept:application/json
```

Result: Shows 200 response
```text
HTTP/1.1 200 OK
transfer-encoding: chunked
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Date: Thu, 09 Jan 2025 16:44:38 GMT
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
Referrer-Policy: no-referrer

{
    "name": "product",
    "profiles": [
        "docker"
    ],
    "label": null,
    "version": null,
    "state": null,
    "propertySources": [
        {
            "name": "file:/configuration-repository/product-docker.yml",
            "source": {
                "spring.data.mongodb.host": "mongodb",
                "server.port": 8080
            }
        },
        {
            "name": "file:/configuration-repository/application-docker.yml",
            "source": {
                "spring.cloud.activate.on-profile": "docker",
                "spring.cloud.stream.kafka.binder.brokers": "kafka",
                "spring.rabbitmq.host": "rabbitmq",
                "app.eureka-server": "eureka",
                "app.auth-server": "auth-server"
            }
        },
        {
            "name": "file:/configuration-repository/product.yml",
            "source": {
                "server.port": 7001,
                "server.error.include-message": "always",
                "spring.application.name": "product",
                "spring.data.mongodb.host": "localhost",
                "spring.data.mongodb.port": 27017,
                "spring.data.mongodb.database": "productdb",
                "spring.cloud.function.definition": "messageProcessor",
                "spring.cloud.stream.default.contentType": "application/json",
                "spring.cloud.stream.bindings.messageProcessor-in-0.destination": "products",
                "spring.cloud.stream.bindings.messageProcessor-in-0.group": "productsGroup",
                "spring.cloud.stream.bindings.messageProcessor-in-0.consumer.max-attempts": 3,
                "spring.cloud.stream.bindings.messageProcessor-in-0.consumer.back-off-initial-interval": 500,
                "spring.cloud.stream.bindings.messageProcessor-in-0.consumer.back-off-max-interval": 1000,
                "spring.cloud.stream.bindings.messageProcessor-in-0.consumer.back-off-multiplier": 2.0,
                "spring.cloud.stream.rabbit.bindings.messageProcessor-in-0.consumer.auto-bind-dlq": true,
                "spring.cloud.stream.rabbit.bindings.messageProcessor-in-0.consumer.republish-to-dlq": true,
                "spring.cloud.stream.kafka.bindings.messageProcessor-in-0.consumer.enable-dlq": true,
                "logging.level.root": "info",
                "logging.level.org.emangini.servolution": "debug",
                "logging.level.org.springframework.data.mongodb.core.ReactiveMongoTemplate": "debug"
            }
        },
        {
            "name": "file:/configuration-repository/application.yml",
            "source": {
                "app.eureka-username": "username",
                "app.eureka-server": "localhost",
                "app.auth-server": "localhost",
                "eureka.client.service-url.defaultZone": "http://${app.eureka-username}:${app.eureka-password}@${app.eureka-server}:8761/eureka/",
                "eureka.client.initial-instance-info-replication-interval-seconds": 5,
                "eureka.client.registry-fetch-interval-seconds": 5,
                "eureka.instance.lease-renewal-interval-in-seconds": 5,
                "eureka.instance.lease-expiration-duration-in-seconds": 5,
                "spring.rabbitmq.host": "127.0.0.1",
                "spring.rabbitmq.port": 5672,
                "spring.rabbitmq.username": "guest",
                "spring.cloud.stream.kafka.binder.brokers": "127.0.0.1",
                "spring.cloud.stream.kafka.binder.defaultBrokerPort": 9092,
                "spring.cloud.stream.default-binder": "rabbit",
                "management.endpoint.health.show-details": "always",
                "management.endpoints.web.exposure.include": "*",
                "app.eureka-password": "password",
                "spring.rabbitmq.password": "guest"
            }
        }
    ]
}
```
---

## Testing Encrypt/Decrypt Endpoints

- [1. Test Encrypt](#1-test-encrypt)
- [2. Test Decrypt](#2-test-decrypt)

### 1. Test Encrypt

#### Curl
```shell
curl -k https://username:password@localhost:8443/config/encrypt --data-urlencode "hello world" -s
```
```text
e302e8cfd253bfdbe744b6aa2c84540d7b51bd2e99a39e3ea2908b80fe2bfe3e
```

#### Httpie
```shell
http --unsorted --verify=no --auth username:password POST https://localhost:8443/config/encrypt  body="hello world" --form
```
```text
HTTP/1.1 200 OK
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 96
Date: Thu, 09 Jan 2025 16:52:41 GMT
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
Referrer-Policy: no-referrer

e9cc69771fb9e2d4fe1a7ce55ee64db83bc37bf4bbd397f4617679255d32dd8ae208c507105a065a75d255403b1d6ecb
```

### 2. Test Decrypt

#### Curl

```shell
curl -k https://username:password@localhost:8443/config/decrypt -d e302e8cfd253bfdbe744b6aa2c84540d7b51bd2e99a39e3ea2908b80fe2bfe3e -s
```

```text
hello world
```