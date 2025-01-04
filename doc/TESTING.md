# Testing the System

## Contents

- [Executing Test Runner](#executing-test-runner)
- [Gradle (CLI, Intellij)](TESTING-GRADLE.md)
- [Manually Testing (based on testRunner.sh)](TESTING-TESTRUNNER.md)  Uses seeded data.
- [Manually testing (bare environment)](TESTING-TESTENVONLY.md) Doesn't use seeded data. 


## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)

# TODO 
- Create a step-by-step for testRunner tests

---
## Executing Test Runner

By default, `testRunner.sh` is set to test using containers.

You can execute tests against local instances of the services by prepending the execution statement prepended by the 
local port
(`PORT=7000 ./testRunner.sh`)
However, you will also need to start all the support containers (mongo, rabbit, mysql etc.) 

```shell
./testRunner.sh start stop
```
```text
Starting Landscape Tests:  Sat Jan 4 12:16:42 AM EST 2025
HOST=localhost
PORT=8080
Restarting test environment...
$ docker-compose down --remove-orphans
[+] Running 9/9
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                         5.3s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                         7.9s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                        10.2s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                         1.1s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                         1.3s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                         0.2s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                         0.1s 
$ docker-compose up -d
[+] Running 9/9
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.1s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                         6.6s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                                                                                                         0.7s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                        21.1s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                         6.2s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                         0.7s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                        21.2s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                         6.8s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                         6.8s 
Wait for: curl http://localhost:8080/actuator/health... , retry #1 , retry #2 , retry #3 {"status":"UP","components":{"coreServices":{"status":"UP","components":{"product":{"status":"UP"},"recommendation":{"status":"UP"},"review":{"status":"UP"}}},"discoveryComposite":{"status":"UP","components":{"discoveryClient":{"status":"UP","details":{"services":["product-composite","product","recommendation","review"]}},"eureka":{"description":"Remote status from Eureka server","status":"UP","details":{"applications":{"PRODUCT-COMPOSITE":1,"PRODUCT":1,"REVIEW":1,"RECOMMENDATION":1}}}}},"diskSpace":{"status":"UP","details":{"total":101129359360,"free":26727686144,"threshold":10485760,"path":"/application/.","exists":true}},"ping":{"status":"UP"},"rabbit":{"status":"UP","details":{"version":"4.0.5"}},"reactiveDiscoveryClients":{"status":"UP","components":{"Simple Reactive Discovery Client":{"status":"UP","details":{"services":[]}},"Spring Cloud Eureka Reactive Discovery Client":{"status":"UP","details":{"services":["product-composite","product","recommendation","review"]}}}},"refreshScope":{"status":"UP"},"ssl":{"status":"UP","details":{"validChains":[],"invalidChains":[]}}}}DONE, continues...
Test OK (HTTP Status: 200)
Test OK (actual value: 4)
Test OK (HTTP Status: 202, )
Test OK (actual value: 202)
Test OK (HTTP Status: 202, )
Test OK (actual value: 202)
Test OK (HTTP Status: 202, )
Test OK (actual value: 202)
Waiting for messages to be processed...
Test OK (HTTP Status: 200)
Test OK (actual value: 1)
Test OK (actual value: 3)
Test OK (actual value: 3)
All messages have been processed
Test OK (HTTP Status: 200)
Test OK (actual value: 1)
Test OK (actual value: 3)
Test OK (actual value: 3)
Test OK (HTTP Status: 404, {"timestamp":"2025-01-04T05:17:27.535758119Z","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2025-01-04T05:17:27.732196327Z","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2025-01-04T05:17:27.768+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"3a50795a-17","message":"Type mismatch."})
Test OK (actual value: "Type mismatch.")
Swagger/OpenAPI tests
Test OK (HTTP Status: 302, )
Test OK (HTTP Status: 200)
Test OK (HTTP Status: 200)
Test OK (HTTP Status: 200)
Test OK (actual value: 3.0.1)
Test OK (HTTP Status: 200)
Tests completed, shutting down test environment...
$ docker-compose down
[+] Running 9/9
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                         5.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                        10.4s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                         1.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                         0.9s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                         0.1s 
End, all tests OK:  Sat Jan 4 12:17:39 AM EST 2025
```

---


