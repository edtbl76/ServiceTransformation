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
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
./testRunner.sh start stop
Starting Landscape Tests:  Fri Jan 10 09:38:53 AM EST 2025
HOST=localhost
PORT=8443
SKIP_CHAOS_TESTS=false
Restarting test environment...
$ docker-compose down --remove-orphans
$ docker-compose up -d
[+] Running 12/12
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.0s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                         6.9s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                         7.0s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                        26.4s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                                                                                                         1.0s 
 ✔ Container servicetransformation-config-server-1      Started                                                                                                                                                                                                                                         1.0s 
 ✔ Container servicetransformation-auth-server-1        Healthy                                                                                                                                                                                                                                        16.4s 
 ✔ Container servicetransformation-gateway-1            Started                                                                                                                                                                                                                                        16.6s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                        16.6s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                        26.4s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                         7.1s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                         7.1s 
Wait for: curl -k https://localhost:8443/actuator/health... , retry #1 , retry #2 , retry #3 , retry #4 , retry #5 {"status":"UP","components":{"clientConfigServer":{"status":"UP","details":{"propertySources":["configserver:file:/configuration-repository/application-docker.yml","configserver:file:/configuration-repository/gateway.yml","configserver:file:/configuration-repository/application.yml","configClient"]}},"discoveryComposite":{"status":"UP","components":{"discoveryClient":{"status":"UP","details":{"services":["gateway","auth-server","product","recommendation","product-composite","review"]}},"eureka":{"description":"Remote status from Eureka server","status":"UP","details":{"applications":{"GATEWAY":1,"AUTH-SERVER":1,"PRODUCT-COMPOSITE":1,"PRODUCT":1,"REVIEW":1,"RECOMMENDATION":1}}}}},"diskSpace":{"status":"UP","details":{"total":101129359360,"free":9537388544,"threshold":10485760,"path":"/application/.","exists":true}},"ping":{"status":"UP"},"reactiveDiscoveryClients":{"status":"UP","components":{"Simple Reactive Discovery Client":{"status":"UP","details":{"services":[]}},"Spring Cloud Eureka Reactive Discovery Client":{"status":"UP","details":{"services":["gateway","auth-server","product","recommendation","product-composite","review"]}}}},"refreshScope":{"status":"UP"},"servicesHealthCheck":{"status":"UP","components":{"auth-server":{"status":"UP"},"product":{"status":"UP"},"product-composite":{"status":"UP"},"recommendation":{"status":"UP"},"review":{"status":"UP"}}},"ssl":{"status":"UP","details":{"validChains":[],"invalidChains":[]}}}}DONE, continues...
ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJHbWdESklOOFlJdUlwYjJZN2l5VXlNMUFJejNraUFwMUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjUxOTk3NywiZXhwIjoxNzM2NjA2Mzc3LCJzY29wZSI6InByb2R1Y3Q6cmVhZCBwcm9kdWN0OndyaXRlIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIiwiYXpwIjoiR21nREpJTjhZSXVJcGIyWTdpeVV5TTFBSXoza2lBcDEifQ.TQiyMRE-qT05XhYV5OluvAUNhxVs73n2RARZsbfsT0czGRI9P1yq_Ws-r1SDeHQAGyiaU5y95dqlqZDuHZ61HWtk2Gq4w5XdxunstnQLG0_hb-mmULeuqtWPn56u738JdY7MkLOmtALSc92IMkK1SmH6GiJqCAHlBAzxnP5IVwe89EkfWC5NU6yjr7qE7eFd9ljxV2Jfly61yRyoo_caPyCRpD5YYTLOFCJjrxrbmy51ga68dswfvoTWvQSSuow9b1FJvQ_7dUKd1Dvhf53ohLssTGfBUv1u-vq12Wb5iCzWazv2RM7pmRLqc1RBrMrG71-uDiUvC-q37QXnMMijcg
Test OK (HTTP Status: 200)
Test OK (actual value: 6)
Test OK (HTTP Status: 200)
Test OK (actual value: hello world)
Writing test data...
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
Test OK (HTTP Status: 404, {"timestamp":"2025-01-10T14:39:41.270491365Z","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2025-01-10T14:39:41.51568311Z","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2025-01-10T14:39:41.568+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"427255ea-16","message":"Type mismatch."})
Test OK (actual value: "Type mismatch.")
Test OK (HTTP Status: 401, )
READER_ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJ5a1FTQjFNUXFSTUdWd21FOWdiV2p1R0p1U3ZXVE05UUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjUxOTk4MSwiZXhwIjoxNzM2NjA2MzgxLCJzY29wZSI6InByb2R1Y3Q6cmVhZCIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6InlrUVNCMU1RcVJNR1Z3bUU5Z2JXanVHSnVTdldUTTlRIn0.FGVA0bygHr8Yu7AmYxcZDbZe0oxjBIyNN7XwWXzubnZSPVWyaFJhGB6A9pcJGvgX0VNnQsprEDReEuarvx8icdOS9W6oIhfHafO6VdHugGisyn9EsvnPYTaKjMC98p32kr3RdVojrA-YTb4NqTdnEDxMG2JG3qvyccwMdwxEZ7zJBWcjJLWFGHPX_2KOiNbXbVvBUb1mCrhQV3Gje768chMdEpiCGGaPm-AHcIZt75BW4QS4_idGEUEm9UZpP0vMy-gxVjrP9u4D45qxDKeePcpUlL6DqW7M2x0MW7yh8GX4HuE4zUTSEdEjf2UMg0jwe5u1c6hagev8sVFbOptXRQ
Test OK (HTTP Status: 200)
Test OK (HTTP Status: 403, )
Swagger/OpenAPI tests
Test OK (HTTP Status: 302, )
Test OK (HTTP Status: 200)
Test OK (HTTP Status: 200)
Test OK (HTTP Status: 200)
Test OK (actual value: 3.0.1)
Test OK (actual value: https://localhost:8443)
Test OK (HTTP Status: 200)
Start Chaos Testing
Test OK (actual value: CLOSED)
Test OK (HTTP Status: 500, {"timestamp":"2025-01-10T14:39:44.559+00:00","path":"/product-composite/1","status":500,"error":"Internal Server Error","requestId":"427255ea-26","message":"Did not observe any item or terminal signal within 2000ms in 'onErrorResume' (and no fallback has been configured)"})
Test OK (actual value: Did not observe any item or terminal signal within 2000ms)
Test OK (HTTP Status: 500, {"timestamp":"2025-01-10T14:39:46.610+00:00","path":"/product-composite/1","status":500,"error":"Internal Server Error","requestId":"427255ea-27","message":"Did not observe any item or terminal signal within 2000ms in 'onErrorResume' (and no fallback has been configured)"})
Test OK (actual value: Did not observe any item or terminal signal within 2000ms)
Test OK (HTTP Status: 500, {"timestamp":"2025-01-10T14:39:48.676+00:00","path":"/product-composite/1","status":500,"error":"Internal Server Error","requestId":"427255ea-28","message":"Did not observe any item or terminal signal within 2000ms in 'onErrorResume' (and no fallback has been configured)"})
Test OK (actual value: Did not observe any item or terminal signal within 2000ms)
Test OK (actual value: OPEN)
Test OK (HTTP Status: 200)
Test OK (actual value: Fallback product1)
Test OK (HTTP Status: 200)
Test OK (actual value: Fallback product1)
Test OK (HTTP Status: 404, {"timestamp":"2025-01-10T14:39:48.948558953Z","path":"/product-composite/13","message":"Product Id: 13 not found in fallback cache.","status":404,"error":"Not Found"})
Test OK (actual value: Product Id: 13 not found in fallback cache.)
Will sleep for 10 seconds waiting for the Circuit Breaker to go Half Open
Test OK (actual value: HALF_OPEN)
Test OK (HTTP Status: 200)
Test OK (actual value: product name C)
Test OK (HTTP Status: 200)
Test OK (actual value: product name C)
Test OK (HTTP Status: 200)
Test OK (actual value: product name C)
Test OK (actual value: CLOSED)
Test OK (actual value: CLOSED_TO_OPEN)
Test OK (actual value: OPEN_TO_HALF_OPEN)
Test OK (actual value: HALF_OPEN_TO_CLOSED)
Tests completed, shutting down test environment...
$ docker-compose down
[+] Running 12/12
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                         5.3s 
 ✔ Container servicetransformation-config-server-1      Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                        10.4s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                         0.4s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                        10.4s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                         1.7s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                         3.2s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                         0.2s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                         1.3s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                         0.1s 
End, all tests OK:  Fri Jan 10 09:40:13 AM EST 2025
```

---


