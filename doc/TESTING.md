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
Starting Landscape Tests:  Sun Jan 5 11:19:22 PM EST 2025
HOST=localhost
PORT=8443
Restarting test environment...
$ docker-compose down --remove-orphans
[+] Running 11/11
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                        10.2s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                         5.4s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                         7.3s 
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                         0.5s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                         3.2s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                         1.3s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                         0.1s 
$ docker-compose up -d
[+] Running 11/11
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                         0.0s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                                                                                                         0.6s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                        21.2s 
 ✔ Container servicetransformation-auth-server-1        Healthy                                                                                                                                                                                                                                         5.7s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                         6.7s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                         6.2s 
 ✔ Container servicetransformation-gateway-1            Started                                                                                                                                                                                                                                         6.2s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                         7.0s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                        21.3s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                         7.0s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                         6.9s 
Wait for: curl -k https://localhost:8443/actuator/health... , retry #1 , retry #2 , retry #3 , retry #4 {"status":"UP","components":{"discoveryComposite":{"status":"UP","components":{"discoveryClient":{"status":"UP","details":{"services":["gateway","auth-server","recommendation","product-composite","product","review"]}},"eureka":{"description":"Remote status from Eureka server","status":"UP","details":{"applications":{"GATEWAY":1,"AUTH-SERVER":1,"PRODUCT-COMPOSITE":1,"PRODUCT":1,"REVIEW":1,"RECOMMENDATION":1}}}}},"diskSpace":{"status":"UP","details":{"total":101129359360,"free":12190720000,"threshold":10485760,"path":"/application/.","exists":true}},"ping":{"status":"UP"},"reactiveDiscoveryClients":{"status":"UP","components":{"Simple Reactive Discovery Client":{"status":"UP","details":{"services":[]}},"Spring Cloud Eureka Reactive Discovery Client":{"status":"UP","details":{"services":["gateway","auth-server","recommendation","product-composite","product","review"]}}}},"refreshScope":{"status":"UP"},"servicesHealthCheck":{"status":"UP","components":{"auth-server":{"status":"UP"},"product":{"status":"UP"},"product-composite":{"status":"UP"},"recommendation":{"status":"UP"},"review":{"status":"UP"}}},"ssl":{"status":"UP","details":{"validChains":[],"invalidChains":[]}}}}DONE, continues...
ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJHbWdESklOOFlJdUlwYjJZN2l5VXlNMUFJejNraUFwMUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjEzNzIxMCwiZXhwIjoxNzM2MjIzNjEwLCJzY29wZSI6InByb2R1Y3Q6cmVhZCBwcm9kdWN0OndyaXRlIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIiwiYXpwIjoiR21nREpJTjhZSXVJcGIyWTdpeVV5TTFBSXoza2lBcDEifQ.UyInbkZzBCAwcP5eARGUGRS1XMS7tR9OoriQfCYHldQVW6Wfn-yAz4sSygwd6msg-5eh61BIAoupOvTYaK5q-PiIaghTNuuOrnQZtzqyK87JZCXKlKr1wT6-odVSKMnfSgjx6fztSuQgSTfmfyQ2rJhM3EShTkviGdWeA4aP7eu8zWrdrrgLVmOB__HOvuRwDWCgUNlU25YWOLDP1GbvKguxmk8cdAvx-LYtdaNcZ6IMtwxbO9vUT8CgM2O2vfp_p9tiE7b-9qxHjDBFCS0hyRAqlFJEdimt2P2epO_Fubk9NUZmbckV0uULgGS9q5HopTB4UyefshbIPnRZtqBJdg
Test OK (HTTP Status: 200)
Test OK (actual value: 6)
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
Test OK (HTTP Status: 404, {"timestamp":"2025-01-06T04:20:13.841135433Z","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2025-01-06T04:20:14.096048977Z","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2025-01-06T04:20:14.149+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"d2632efb-17","message":"Type mismatch."})
Test OK (actual value: "Type mismatch.")
Test OK (HTTP Status: 401, )
READER_ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJ5a1FTQjFNUXFSTUdWd21FOWdiV2p1R0p1U3ZXVE05UUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjEzNzIxNCwiZXhwIjoxNzM2MjIzNjE0LCJzY29wZSI6InByb2R1Y3Q6cmVhZCIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6InlrUVNCMU1RcVJNR1Z3bUU5Z2JXanVHSnVTdldUTTlRIn0.oXDbTXRzyHqFITjbbBuphlY9SV5l4AdofzwdXPIDnlyH-aikXpzgg_TOjWYHawk0pZ2efETDupAusqKP1Obbh6LtPHtyKp4_kW0yw4lWB3I2BLbSTZJq74IwJOKQ9zHBEih__vJPpM01Lr5fH94LUTujKJGvfp-wmZSYgo6fY0mLN_l9H9KBnS1hYhd-PXqOHYNqhAJ0xNb0sMlBFQxv4DSBkoYaTV4F9ALbPzlU4jfucGj71D5BkkIHbBXzEMRPLypmhzN3Ml_ZsGL-S7jmgKPNuj6cMljP9T1ZoMNqzkevGs7kgfe36U1xBjFWOfzGq58dAvM0mlFwshR-RcY2CQ
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
Tests completed, shutting down test environment...
$ docker-compose down
[+] Running 11/11
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                         8.2s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                        10.2s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                         0.3s 
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                         5.3s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                        10.3s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                         8.1s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                         0.2s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                         1.7s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                         3.2s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                         1.3s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                         0.1s 
End, all tests OK:  Sun Jan 5 11:20:28 PM EST 2025
```

---


