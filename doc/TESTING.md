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
Starting Landscape Tests:  Sun Jan 5 12:59:55 PM EST 2025
HOST=localhost
PORT=8443
Restarting test environment...
$ docker-compose down --remove-orphans
[+] Running 11/11
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                            10.4s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                            10.3s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                             0.6s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                            10.4s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                            10.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                             8.3s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                             0.3s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                             3.2s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                             1.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                             1.8s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                             0.1s 
$ docker-compose up -d
[+] Running 11/11
 ✔ Network servicetransformation_default                Created                                                                                                                                                                                                                                             0.1s 
 ✔ Container servicetransformation-mongodb-1            Healthy                                                                                                                                                                                                                                             6.2s 
 ✔ Container servicetransformation-auth-server-1        Healthy                                                                                                                                                                                                                                             5.7s 
 ✔ Container servicetransformation-mysql-1              Healthy                                                                                                                                                                                                                                            21.2s 
 ✔ Container servicetransformation-eureka-1             Started                                                                                                                                                                                                                                             0.6s 
 ✔ Container servicetransformation-rabbitmq-1           Healthy                                                                                                                                                                                                                                             6.2s 
 ✔ Container servicetransformation-gateway-1            Started                                                                                                                                                                                                                                             6.1s 
 ✔ Container servicetransformation-product-composite-1  Started                                                                                                                                                                                                                                             6.3s 
 ✔ Container servicetransformation-review-1             Started                                                                                                                                                                                                                                            21.3s 
 ✔ Container servicetransformation-recommendation-1     Started                                                                                                                                                                                                                                             6.4s 
 ✔ Container servicetransformation-product-1            Started                                                                                                                                                                                                                                             6.4s 
Wait for: curl -k https://localhost:8443/actuator/health... , retry #1 , retry #2 , retry #3 , retry #4 {"status":"UP","components":{"discoveryComposite":{"status":"UP","components":{"discoveryClient":{"status":"UP","details":{"services":["gateway","auth-server","product-composite","product","recommendation","review"]}},"eureka":{"description":"Remote status from Eureka server","status":"UP","details":{"applications":{"GATEWAY":1,"AUTH-SERVER":1,"PRODUCT-COMPOSITE":1,"PRODUCT":1,"REVIEW":1,"RECOMMENDATION":1}}}}},"diskSpace":{"status":"UP","details":{"total":101129359360,"free":16949256192,"threshold":10485760,"path":"/application/.","exists":true}},"ping":{"status":"UP"},"reactiveDiscoveryClients":{"status":"UP","components":{"Simple Reactive Discovery Client":{"status":"UP","details":{"services":[]}},"Spring Cloud Eureka Reactive Discovery Client":{"status":"UP","details":{"services":["gateway","auth-server","product-composite","product","recommendation","review"]}}}},"refreshScope":{"status":"UP"},"servicesHealthCheck":{"status":"UP","components":{"auth-server":{"status":"UP"},"product":{"status":"UP"},"product-composite":{"status":"UP"},"recommendation":{"status":"UP"},"review":{"status":"UP"}}},"ssl":{"status":"UP","details":{"validChains":[],"invalidChains":[]}}}}DONE, continues...
ACCESS_TOKEN=eyJraWQiOiIyYmY1YzQwYy04NzMzLTRiZmEtOTMwMS1hYTQ0OTJlMDZmYWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3cml0ZXIiLCJhdWQiOiJ3cml0ZXIiLCJuYmYiOjE3MzYxMDAwNDQsInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJwcm9kdWN0OnJlYWQiXSwiaXNzIjoiaHR0cDovL2F1dGgtc2VydmVyOjk5OTkiLCJleHAiOjE3MzYxMDM2NDQsImlhdCI6MTczNjEwMDA0NH0.T7zEqWcBs7i7ppqXwIMubIPAgRIDFmJutUccWKprSlw93XUx8zkufzU_PUPMjq7DvTJ8XOtiXvUL6ekIW6ziDHiJx52R_5YsRKM6pyALYduroMg2HWof4c6dppOZq1KEmoeY1W0dFZV2-I8b6-ujRKu7zIzxPmLvMLCwiuwABqe2qB1t8wT5cEvGLDoFAv4EsrZ75s4udqQ1D1t5pji1rErItR4RVv9CXIP8wt1yh7Mtpm9uthtAsXRYtfGdQlteQ9sCwk77EEacvZnJ8KnJ_x_VQ8qsWHGyTj1TdWaU2UrjLqB3KapP2e3f5ACHZLs-umGev-75-rKeq_nhphdORQ
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
Test OK (HTTP Status: 404, {"timestamp":"2025-01-05T18:00:46.574638687Z","path":"/product-composite/13","message":"No product found for productId: 13","status":404,"error":"Not Found"})
Test OK (actual value: No product found for productId: 13)
Test OK (HTTP Status: 200)
Test OK (actual value: 113)
Test OK (actual value: 0)
Test OK (actual value: 3)
Test OK (HTTP Status: 200)
Test OK (actual value: 213)
Test OK (actual value: 3)
Test OK (actual value: 0)
Test OK (HTTP Status: 422, {"timestamp":"2025-01-05T18:00:46.821806044Z","path":"/product-composite/-1","message":"Invalid productId: -1","status":422,"error":"Unprocessable Entity"})
Test OK (actual value: "Invalid productId: -1")
Test OK (HTTP Status: 400, {"timestamp":"2025-01-05T18:00:46.873+00:00","path":"/product-composite/invalidProductId","status":400,"error":"Bad Request","requestId":"730db161-17","message":"Type mismatch."})
Test OK (actual value: "Type mismatch.")
Test OK (HTTP Status: 401, )
READER_ACCESS_TOKEN=eyJraWQiOiIyYmY1YzQwYy04NzMzLTRiZmEtOTMwMS1hYTQ0OTJlMDZmYWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWFkZXIiLCJhdWQiOiJyZWFkZXIiLCJuYmYiOjE3MzYxMDAwNDYsInNjb3BlIjpbInByb2R1Y3Q6cmVhZCJdLCJpc3MiOiJodHRwOi8vYXV0aC1zZXJ2ZXI6OTk5OSIsImV4cCI6MTczNjEwMzY0NiwiaWF0IjoxNzM2MTAwMDQ2fQ.gOtp1kFOU1BnwZV8e7L5Va7PQABjsmVC4jEdAQLWhutWAQDALXYSVoEV5TePCbHMT5CDHfh89sWgDeEsPKHFAQZnFZRQnS48YcSe4dSoHssGna09dimUo8BSE42NV4Re64nrZkK7bC7FDuAH2HELYusaE4wBCi-0gzElbuYC6I7zS4qzbJLmUyu6EJqe3nwNJfRhm2sn1WXo6whmG2ZQ9VFJDcbpUIyuSUfNETJDvxsRAL0YnOTTbblIp5GtUw-9RN71t2j6efFHHpbEd-uZiQQ9WvT6TBzI1ei51I77bielvmYaXJFeAdqOEstRJl8FLUy1pBOKOIbn4immPU5z7A
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
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                             5.3s 
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                             0.4s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                             8.3s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                             8.3s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                            10.3s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                            10.2s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                             0.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                             0.9s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                             1.3s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                             3.2s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                             0.1s 
End, all tests OK:  Sun Jan 5 01:01:01 PM EST 2025
```

---


