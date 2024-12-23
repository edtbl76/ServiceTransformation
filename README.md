# ServiceTransformation v1.0.1

## Description
- Simple Services (local build, local execution)
- No Persistence
- Simple, Manually Executed Tests

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