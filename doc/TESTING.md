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
Starting Landscape Tests:  Thu Jan 9 11:02:44 AM EST 2025
HOST=localhost
PORT=8443
Restarting test environment...
$ docker-compose down --remove-orphans
$ docker-compose up -d                                                               
[+] Running 12/12                                                          18.9s 
 ✔ Network servicetransformation_default                Created                                                                                                                                                        0.0s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                        0.7s 
 ✔ Container servicetransformation-config-server-1      Started                                                                                                                                                        0.8s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                        6.3s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                        6.7s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                       21.2s 
 ✔ Container servicetransformation-auth-server-1        Healthy                                                                                                                                                       11.3s 
 ✔ Container servicetransformation-gateway-1            Started                                                                                                                                                       11.5s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                       11.5s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                       21.3s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                        6.9s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                        6.9s 
Wait for: curl -k https://localhost:8443/actuator/health... , retry #1 , retry #2 , retry #3 , retry #4 {"status":"UP","components":{"clientConfigServer":{"status":"UP","details":{"propertySources":["configserver:file:/configuration-repository/application-docker.yml","configserver:file:/configuration-repository/gateway.yml","configserver:file:/configuration-repository/application.yml","configClient"]}},"discoveryComposite":{"status":"UP","components":{"discoveryClient":{"status":"UP","details":{"services":["auth-server","recommendation","product","gateway","product-composite","review"]}},"eureka":{"description":"Remote status from Eureka server","status":"UP","details":{"applications":{"GATEWAY":1,"AUTH-SERVER":1,"PRODUCT-COMPOSITE":1,"PRODUCT":1,"REVIEW":1,"RECOMMENDATION":1}}}}},"diskSpace":{"status":"UP","details":{"total":101129359360,"free":10742480896,"threshold":10485760,"path":"/application/.","exists":true}},"ping":{"status":"UP"},"reactiveDiscoveryClients":{"status":"UP","components":{"Simple Reactive Discovery Client":{"status":"UP","details":{"services":[]}},"Spring Cloud Eureka Reactive Discovery Client":{"status":"UP","details":{"services":["auth-server","recommendation","product","gateway","product-composite","review"]}}}},"refreshScope":{"status":"UP"},"servicesHealthCheck":{"status":"UP","components":{"auth-server":{"status":"UP"},"product":{"status":"UP"},"product-composite":{"status":"UP"},"recommendation":{"status":"UP"},"review":{"status":"UP"}}},"ssl":{"status":"UP","details":{"validChains":[],"invalidChains":[]}}}}DONE, continues...
ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJHbWdESklOOFlJdUlwYjJZN2l5VXlNMUFJejNraUFwMUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjQzODU5OSwiZXhwIjoxNzM2NTI0OTk5LCJzY29wZSI6InByb2R1Y3Q6cmVhZCBwcm9kdWN0OndyaXRlIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIiwiYXpwIjoiR21nREpJTjhZSXVJcGIyWTdpeVV5TTFBSXoza2lBcDEifQ.iS8URlXd5zeBO9RygGrqVuZn9jr5_tWwZZkbpBfq36zpHvb-jmdiywxQrxlManerf4tpsp5yzVIUx-Kub15L4ezopIZzRDwz01UaJEzrYWi9m8HhjCWqCROKs14xG7ILSkkouELUxFpzY6LV41C9jK11GwL_IDK2tDvzIuN91kuhtSFbFOvOMf2rZes_VPyuPUVYc7itc4dY0l-jRsx7ig02fjo4aq-xkdmM7Z4J1MXMxKdECYBpSxS47bCJryYSTKc7M7NCfvqQIkjbkYLn54W067IMJKHd4vwv1wXYJ1TWopEi9tTQdEcsi6gs-NUKdPrAVyoSdf1fiS2UHLUHmg
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
Test OK (HTTP Status: 404, {"timestamp":"2025-01-09T16:03:23.394323943Z","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2025-01-09T16:03:23.63918411Z","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2025-01-09T16:03:23.702+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"832dfa2b-17","message":"Type mismatch."})
Test OK (actual value: "Type mismatch.")
Test OK (HTTP Status: 401, )
READER_ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9OeUYzSF9qdmZ4LTYySFVCNXE2NCJ9.eyJpc3MiOiJodHRwczovL2Rldi1rMjZtd3cyMGM4ODJpcnY2LnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJ5a1FTQjFNUXFSTUdWd21FOWdiV2p1R0p1U3ZXVE05UUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9wcm9kdWN0LWNvbXBvc2l0ZSIsImlhdCI6MTczNjQzODYwMywiZXhwIjoxNzM2NTI1MDAzLCJzY29wZSI6InByb2R1Y3Q6cmVhZCIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6InlrUVNCMU1RcVJNR1Z3bUU5Z2JXanVHSnVTdldUTTlRIn0.Ygf5JqITXre31p7YssicoP2BOoHeEHsdUVeEEa-vYvWkAvXt4MftbtRPK3YOOprqKvGwa9uHcRt0ys9TpvHAvtUBU5U8R_Y_89ILGacLd4CrubOHxlBHzwZYOJWpnTVbmRchiE8QFXX3fOZ0qb0zcslWG-u5o0K_HMvE-vcaLcph1PGtSMjmlzkqC0PdztWM3vsnQ4964LUGrqSnnecT8nAVJCPlfVeAJpJ8txKoFMPLJH3ACbJo8GaMPJKpCKaFo6No_Q8FXYHXvmKnRGZvCUG92CjrDzHiR37jTq-CAV7MqvNROtV7fCeFWqE21jEsVAYY6uodvZMVYw3oG8P5zA
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
[+] Running 12/12
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                        0.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                        8.0s 
 ✔ Container servicetransformation-config-server-1      Removed                                                                                                                                                        0.3s 
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                       10.3s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                       10.3s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                       10.3s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                        8.0s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                        0.2s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                        1.1s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                        1.3s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                        3.2s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                        0.1s 
End, all tests OK:  Thu Jan 9 11:03:38 AM EST 2025
```

---


