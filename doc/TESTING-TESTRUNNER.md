# Testing with Test Runner


## Contents

- [Starting the test environment](#start-the-environment)
- [Setting Auth Token ENV](#setting-the-auth-token-environment-variables)
- [Checking Liveness](#verifying-system-health)
- [Verifying a Normal Request](#verifying-a-normal-request)
- [Verifying Behavior for Nonexistent Product](#verify-behavior-for-nonexistent-productid)
- [Verifying Request for Product w/o Recommendations](#verify-request-for-product-without-recommendations)
- [Verifying Request for Product w/o Reviews](#verify-request-for-product-without-reviews)
- [Verifying Behavior for Invalid Product Ids](#verify-behavior-for-invalid-product-ids)
- [Verifying Behavior for Non-Numeric Product Ids](#verify-behavior-for-nonnumeric-product-ids)
- [Verifying 401 Unauthorized](#verifying-401-unauthorized)
- [Verifying Read Token Constraints](#verifying-read-token-constraints)
- [Verifying Access to OpenAPI Urls](#verifying-access-to-openapi-urls)
- [Shutting down the test environment](#shutting-down-test-environment)


---
## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)

---

## Start the environment

To start the test environment and seed data w/o stopping containers: 
```shell
./testRunner.sh start
```
---

## Setting the Auth Token Environment Variables

- [Setting WRITE_ACCESS_TOKEN](#setting-write_access_token)
- [Setting READ_ACCESS_TOKEN](#setting-read_access_token)

### Setting WRITE_ACCESS_TOKEN
```shell
export WRITE_ACCESS_TOKEN=$(curl -k https://writer:writer@localhost:8443/oauth2/token -d grant_type=client_credentials -s -d scope="product:write product:read" |  jq .access_token -r)
echo $WRITE_ACCESS_TOKEN
```
```text
eyJraWQiOiI1ZDU3MDhkMi0xNGQ0LTRhNGYtOGZhNy04YTNiMmNhY2RlYjAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3cml0ZXIiLCJhdWQiOiJ3cml0ZXIiLCJuYmYiOjE3MzYxMDI0MDgsInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJwcm9kdWN0OnJlYWQiXSwiaXNzIjoiaHR0cDovL2F1dGgtc2VydmVyOjk5OTkiLCJleHAiOjE3MzYxMDYwMDgsImlhdCI6MTczNjEwMjQwOH0.DTNdeReyWE90OriaX0-gwD8bEbEpFIjKXT5oSqQWsY4bHqxTIDQ6l80Jha58NXKgxG-KUKQDvwTW1EqR0Cus3fkgNwz_vmMZbyeQsmEV-vAbmDNalLFeYT8z6bSjqF-n-bls85HU9YACghbRREgcxUEXblX7gUsXw2S--2xfRlJ6wZR0rJL4r5Fn0Ds972JuCnnWkqcpXMwuDznAvWi_Tu7KnxvG43l8Nbzs3Mqna_xwfgigZpANPEA94lzLxZtgI0LNNsM1i3WPoy2fpwjZERnGYHO5kyBNUdmBQlMlazJyJXDwoymXIhh1Qm7_AESCRsCjWkwvIvaNLCeES_hzgg
```

### Setting READ_ACCESS_TOKEN
```shell
export READ_ACCESS_TOKEN=$(curl -k https://reader:reader@localhost:8443/oauth2/token -d grant_type=client_credentials -d scope="product:read" -s | jq .access_token -r)
echo $READ_ACCESS_TOKEN
```

```text
eyJraWQiOiI1ZDU3MDhkMi0xNGQ0LTRhNGYtOGZhNy04YTNiMmNhY2RlYjAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWFkZXIiLCJhdWQiOiJyZWFkZXIiLCJuYmYiOjE3MzYxMDc2OTUsInNjb3BlIjpbInByb2R1Y3Q6cmVhZCJdLCJpc3MiOiJodHRwOi8vYXV0aC1zZXJ2ZXI6OTk5OSIsImV4cCI6MTczNjExMTI5NSwiaWF0IjoxNzM2MTA3Njk1fQ.GczMjjML8OBJPrwRRKNNnUOgLeswOZsPJmLle_NtczUDY236LXJ_KNquYBON0ylUS1aXNd-cTF93OOwAztFpfZNLStDrut3wmjMnnRx1eVN9-eWL75RpcYb7U6kmoOkXm1f5_V-0DCu5-nuEn646fhuDQnPkPHn7FOLYAOpX0RKEZDdt3LgTE9rv1DL2qHV0ySFtvJwwYsZvvNVVKU1Kaf8H5X9Xy67kE-HtwHqo_qb0lSQU9jFtWnEInfYeNJd_4mYnrDkDVzHSQYw92oZHndbuEJzADM1h3PYI65UJLhOZH710Do94iJ3dB7ABkIkIFflmyag0dLzzDRRFpnKOeg
```

---

## Verifying System Health
The healthcheck ensures that the the Spring Services are up. 

NOTE: Links to Spring Actuator endpoints can be found in the [README](../README.md).

- [1. Actuator Healthcheck](#1-actuator-healthcheck)
- [2. Validating Service Discovery](#2-validating-service-discovery)
- [3. Validating 200](#3-validate-200-service-discovery)
- [4. Validating Number of Registered Instances](#4-validate-number-of-registered-instances)


### 1. Actuator Healthcheck


```shell
## curl
curl -k https://localhost:8443/actuator/health -s | jq
## httpie
http https://localhost:8443/actuator/health --unsorted --verify=no
```

NOTE: 
- Output shows httpie only
- compare the headers to previous versions of this document. 
```text
HTTP/1.1 200 OK
Content-Type: application/vnd.spring-boot.actuator.v3+json
Content-Length: 1186
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "status": "UP",
    "components": {
        "discoveryComposite": {
            "status": "UP",
            "components": {
                "discoveryClient": {
                    "status": "UP",
                    "details": {
                        "services": [
                            "gateway",
                            "auth-server",
                            "product",
                            "recommendation",
                            "product-composite",
                            "review"
                        ]
                    }
                },
                "eureka": {
                    "description": "Remote status from Eureka server",
                    "status": "UP",
                    "details": {
                        "applications": {
                            "GATEWAY": 1,
                            "AUTH-SERVER": 1,
                            "PRODUCT-COMPOSITE": 1,
                            "PRODUCT": 1,
                            "REVIEW": 1,
                            "RECOMMENDATION": 1
                        }
                    }
                }
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 101129359360,
                "free": 15852122112,
                "threshold": 10485760,
                "path": "/application/.",
                "exists": true
            }
        },
        "ping": {
            "status": "UP"
        },
        "reactiveDiscoveryClients": {
            "status": "UP",
            "components": {
                "Simple Reactive Discovery Client": {
                    "status": "UP",
                    "details": {
                        "services": []
                    }
                },
                "Spring Cloud Eureka Reactive Discovery Client": {
                    "status": "UP",
                    "details": {
                        "services": [
                            "gateway",
                            "auth-server",
                            "product",
                            "recommendation",
                            "product-composite",
                            "review"
                        ]
                    }
                }
            }
        },
        "refreshScope": {
            "status": "UP"
        },
        "servicesHealthCheck": {
            "status": "UP",
            "components": {
                "auth-server": {
                    "status": "UP"
                },
                "product": {
                    "status": "UP"
                },
                "product-composite": {
                    "status": "UP"
                },
                "recommendation": {
                    "status": "UP"
                },
                "review": {
                    "status": "UP"
                }
            }
        },
        "ssl": {
            "status": "UP",
            "details": {
                "validChains": [],
                "invalidChains": []
            }
        }
    }
}
```
---

### 2. Getting List of Registered Instances

```shell
# curl
curl -H "accept:application/json" -k https://username:password@localhost:8443/eureka/api/apps -s | jq -r .applications.application[].instance[].instanceId 
# httpie
http --auth username:password https://localhost:8443/eureka/api/apps Accept:application/json --unsorted --verify=no | jq -r .applications.application[].instance[].instanceId

```

```text
edd4ca3b68e6:gateway:8443
ecd995a37543:auth-server:9999
1b2ed622e699:product-composite:8080
1cbd3f05193c:product:8080
085aabe4c5d7:review:8080
726df867dba4:recommendation:8080
```



---
### 3. Validating Service Discovery (Complete)


```shell
## curl
curl -H "accept:application/json" -k https://username:password@localhost:8443/eureka/api/apps -s | jq
## httpie
http --auth username:password https://localhost:8443/eureka/api/apps Accept:application/json --unsorted --verify=no
```

NOTE: if you don't add the accept:application/json part then this will show in XML (very ugly!)
```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --auth username:password https://localhost:8443/eureka/api/apps Accept:application/json --unsorted --verify=no
HTTP/1.1 200 OK
Content-Encoding: gzip
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Content-Length: 1025
Date: Sun, 05 Jan 2025 18:25:09 GMT
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
Referrer-Policy: no-referrer

{
    "applications": {
        "versions__delta": "1",
        "apps__hashcode": "UP_6_",
        "application": [
            {
                "name": "GATEWAY",
                "instance": [
                    {
                        "instanceId": "84d711deb45f:gateway:8443",
                        "hostName": "84d711deb45f",
                        "app": "GATEWAY",
                        "ipAddr": "172.25.0.7",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 8443,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100982320,
                            "lastRenewalTimestamp": 1736101362453,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100982320
                        },
                        "metadata": {
                            "management.port": "8443"
                        },
                        "homePageUrl": "http://84d711deb45f:8443/",
                        "statusPageUrl": "http://84d711deb45f:8443/actuator/info",
                        "healthCheckUrl": "http://84d711deb45f:8443/actuator/health",
                        "vipAddress": "gateway",
                        "secureVipAddress": "gateway",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100982320",
                        "lastDirtyTimestamp": "1736100982143",
                        "actionType": "ADDED"
                    }
                ]
            },
            {
                "name": "AUTH-SERVER",
                "instance": [
                    {
                        "instanceId": "725b11bad881:auth-server:9999",
                        "hostName": "725b11bad881",
                        "app": "AUTH-SERVER",
                        "ipAddr": "172.25.0.3",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 9999,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100980433,
                            "lastRenewalTimestamp": 1736101360615,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100980434
                        },
                        "metadata": {
                            "management.port": "9999"
                        },
                        "homePageUrl": "http://725b11bad881:9999/",
                        "statusPageUrl": "http://725b11bad881:9999/actuator/info",
                        "healthCheckUrl": "http://725b11bad881:9999/actuator/health",
                        "vipAddress": "auth-server",
                        "secureVipAddress": "auth-server",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100980434",
                        "lastDirtyTimestamp": "1736100980255",
                        "actionType": "ADDED"
                    }
                ]
            },
            {
                "name": "PRODUCT-COMPOSITE",
                "instance": [
                    {
                        "instanceId": "046d084ab698:product-composite:8080",
                        "hostName": "046d084ab698",
                        "app": "PRODUCT-COMPOSITE",
                        "ipAddr": "172.25.0.10",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 8080,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100985325,
                            "lastRenewalTimestamp": 1736101360345,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100985325
                        },
                        "metadata": {
                            "management.port": "8080"
                        },
                        "homePageUrl": "http://046d084ab698:8080/",
                        "statusPageUrl": "http://046d084ab698:8080/actuator/info",
                        "healthCheckUrl": "http://046d084ab698:8080/actuator/health",
                        "vipAddress": "product-composite",
                        "secureVipAddress": "product-composite",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100985326",
                        "lastDirtyTimestamp": "1736100985183",
                        "actionType": "ADDED"
                    }
                ]
            },
            {
                "name": "PRODUCT",
                "instance": [
                    {
                        "instanceId": "3467817bf513:product:8080",
                        "hostName": "3467817bf513",
                        "app": "PRODUCT",
                        "ipAddr": "172.25.0.8",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 8080,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100984989,
                            "lastRenewalTimestamp": 1736101360029,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100984990
                        },
                        "metadata": {
                            "management.port": "8080"
                        },
                        "homePageUrl": "http://3467817bf513:8080/",
                        "statusPageUrl": "http://3467817bf513:8080/actuator/info",
                        "healthCheckUrl": "http://3467817bf513:8080/actuator/health",
                        "vipAddress": "product",
                        "secureVipAddress": "product",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100984990",
                        "lastDirtyTimestamp": "1736100984874",
                        "actionType": "ADDED"
                    }
                ]
            },
            {
                "name": "REVIEW",
                "instance": [
                    {
                        "instanceId": "9adb586447ee:review:8080",
                        "hostName": "9adb586447ee",
                        "app": "REVIEW",
                        "ipAddr": "172.25.0.11",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 8080,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100997250,
                            "lastRenewalTimestamp": 1736101362154,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100997250
                        },
                        "metadata": {
                            "management.port": "8080"
                        },
                        "homePageUrl": "http://9adb586447ee:8080/",
                        "statusPageUrl": "http://9adb586447ee:8080/actuator/info",
                        "healthCheckUrl": "http://9adb586447ee:8080/actuator/health",
                        "vipAddress": "review",
                        "secureVipAddress": "review",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100997250",
                        "lastDirtyTimestamp": "1736100997154",
                        "actionType": "ADDED"
                    }
                ]
            },
            {
                "name": "RECOMMENDATION",
                "instance": [
                    {
                        "instanceId": "3488efe82b87:recommendation:8080",
                        "hostName": "3488efe82b87",
                        "app": "RECOMMENDATION",
                        "ipAddr": "172.25.0.9",
                        "status": "UP",
                        "overriddenStatus": "UNKNOWN",
                        "port": {
                            "$": 8080,
                            "@enabled": "true"
                        },
                        "securePort": {
                            "$": 443,
                            "@enabled": "false"
                        },
                        "countryId": 1,
                        "dataCenterInfo": {
                            "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                            "name": "MyOwn"
                        },
                        "leaseInfo": {
                            "renewalIntervalInSecs": 5,
                            "durationInSecs": 5,
                            "registrationTimestamp": 1736100985050,
                            "lastRenewalTimestamp": 1736101360028,
                            "evictionTimestamp": 0,
                            "serviceUpTimestamp": 1736100985050
                        },
                        "metadata": {
                            "management.port": "8080"
                        },
                        "homePageUrl": "http://3488efe82b87:8080/",
                        "statusPageUrl": "http://3488efe82b87:8080/actuator/info",
                        "healthCheckUrl": "http://3488efe82b87:8080/actuator/health",
                        "vipAddress": "recommendation",
                        "secureVipAddress": "recommendation",
                        "isCoordinatingDiscoveryServer": "false",
                        "lastUpdatedTimestamp": "1736100985050",
                        "lastDirtyTimestamp": "1736100984922",
                        "actionType": "ADDED"
                    }
                ]
            }
        ]
    }
}
```

### 4. Validate 200 (Service Discovery)

Commands
```shell
## curl
curl -H "accept:application/json" -k https://username:password@localhost:8443/eureka/api/apps -s --head
## httpie
http --auth username:password https://localhost:8443/eureka/api/apps Accept:application/json --unsorted --verify=no -h
```


NOTE: (AS mentioned before, the default Content-Type is xml, you can see this here.)
```text
HTTP/1.1 200 OK
Content-Encoding: gzip
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Content-Length: 1025
Date: Sun, 05 Jan 2025 18:29:26 GMT
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
Referrer-Policy: no-referrer
```

### 5. Validate Number of Registered Instances

Commands
```shell
## curl
curl -H "accept:application/json" -k https://username:password@localhost:8443/eureka/api/apps -s | jq ".applications.application | length"
## httpie
http --auth username:password https://localhost:8443/eureka/api/apps Accept:application/json --unsorted --verify=no | jq ".applications.application | length"

```

Result:
(You can go back up to the 'full' output to validate) Remember, we have 3 core microservices, the composite, the gateway and the auth-server
```text
6
```


---

## Verifying a Normal Request

- [1. Validate 200](#1-validate-httpstatuscode-is-200)
- [2. Validate Correct ProductId](#2-validate-that-the-retrieved-product-id-is-1)
- [3. Validate Number of Recommendations](#3-validate-that-there-are-3-total-recommendations)
- [4. Validate Number of Reviews](#4-validate-that-there-are-3-total-reviews)


Context of the test

```text
PRODUCT_ID: 1
Recommendations: 1,2,3
Reviews: 1,2,3
```

The following command gets the entire composite output

```shell
# curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```

```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 764
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

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
            "recommendationId": 3,
            "author": "author 3",
            "rate": 3,
            "content": "content 3"
        },
        {
            "recommendationId": 2,
            "author": "author 2",
            "rate": 2,
            "content": "content 2"
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
        "compositeAddress": "046d084ab698/172.25.0.10:8080",
        "productAddress": "3467817bf513/172.25.0.8:8080",
        "recommendationAddress": "9adb586447ee/172.25.0.11:8080",
        "reviewAddress": "3488efe82b87/172.25.0.9:8080"
    }
}
```

Gateway Log
```text
2025-01-05T20:54:56.634Z TRACE 1 --- [gateway] [r-http-epoll-11] o.s.w.s.adapter.HttpWebHandlerAdapter    : [43407e20-32] HTTP GET "/product-composite/1", headers=[Host:"localhost:8443", User-Agent:"HTTPie/2.6.0", Accept-Encoding:"gzip, deflate", Accept:"*/*", Connection:"keep-alive", Authorization:"Bearer eyJraWQiOiJmMWViOWVjMC1iMzI2LTQ3ODgtYTQ4OC0yMjhiOWNjMmZjYWYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3cml0ZXIiLCJhdWQiOiJ3cml0ZXIiLCJuYmYiOjE3MzYxMTA0NzksInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJwcm9kdWN0OnJlYWQiXSwiaXNzIjoiaHR0cDovL2F1dGgtc2VydmVyOjk5OTkiLCJleHAiOjE3MzYxMTQwNzksImlhdCI6MTczNjExMDQ3OX0.lZmo3pOSbPlojW3KLmW8Fw3tQRH_ZbWaCmLTMipgivF0upjN7txjkKX4nPZfJOZI5CPD6Gxloxd15nteBAg_j-FznE401kkbDZpCZPeNtCP2_pi4rFO8ba5zhQNpwkqVc2Sti4SngrDnRyDa0bdHo7dy4NJzvhrRuIGwiHGB9YLOFzDbAY64cFT8wk7oDXnAuOJY2SA1IvS6f2F1lfoD5oLr9lAQy_m2Wby_mmXRL8sY_6vFwaBcueBQ_1gwWEnO1WfAuZ2MEA7uAPJPuZ7Hsyc4lNT5Q__JcgHznsyjfZuSp-LdOjBP1vKljd7tDTKo6funyumwtJkq2QCRMDVzEA"]
2025-01-05T20:54:56.636852409Z 2025-01-05T20:54:56.636Z TRACE 1 --- [gateway] [    parallel-11] o.s.c.g.f.WeightCalculatorWebFilter      : Weights attr: {}
2025-01-05T20:54:56.637361744Z 2025-01-05T20:54:56.637Z TRACE 1 --- [gateway] [    parallel-11] o.s.c.g.h.p.PathRoutePredicateFactory    : Pattern "/product-composite/**" matches against value "/product-composite/1"
2025-01-05T20:54:56.637367991Z 2025-01-05T20:54:56.637Z DEBUG 1 --- [gateway] [    parallel-11] o.s.c.g.h.RoutePredicateHandlerMapping   : Route matched: product-composite
2025-01-05T20:54:56.637369509Z 2025-01-05T20:54:56.637Z DEBUG 1 --- [gateway] [    parallel-11] o.s.c.g.h.RoutePredicateHandlerMapping   : Mapping [Exchange: GET https://localhost:8443/product-composite/1] to Route{id='product-composite', uri=lb://product-composite, order=0, predicate=Paths: [/product-composite/**], match trailing slash: true, gatewayFilters=[], metadata={}}
2025-01-05T20:54:56.637381398Z 2025-01-05T20:54:56.637Z DEBUG 1 --- [gateway] [    parallel-11] o.s.c.g.h.RoutePredicateHandlerMapping   : [43407e20-32] Mapped to org.springframework.cloud.gateway.handler.FilteringWebHandler@165aa43a
2025-01-05T20:54:56.637404488Z 2025-01-05T20:54:56.637Z DEBUG 1 --- [gateway] [    parallel-11] o.s.c.g.handler.FilteringWebHandler      : Sorted gatewayFilterFactories: [[GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.RemoveCachedBodyFilter@250a946}, order = -2147483648], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.AdaptCachedBodyGlobalFilter@aa0dbca}, order = -2147482648], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.NettyWriteResponseFilter@1851c7d2}, order = -1], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.ForwardPathFilter@3982206a}, order = 0], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.GatewayMetricsFilter@4f3fec43}, order = 0], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter@5fe5c68b}, order = 10000], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter@7f2ca6f8}, order = 10150], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.LoadBalancerServiceInstanceCookieFilter@79d49790}, order = 10151], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.WebsocketRoutingFilter@3e9fb485}, order = 2147483646], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.NettyRoutingFilter@13dc6649}, order = 2147483647], [GatewayFilterAdapter{delegate=org.springframework.cloud.gateway.filter.ForwardRoutingFilter@2ffb0d10}, order = 2147483647]]
2025-01-05T20:54:56.637484224Z 2025-01-05T20:54:56.637Z TRACE 1 --- [gateway] [    parallel-11] o.s.c.g.filter.RouteToRequestUrlFilter   : RouteToRequestUrlFilter start
2025-01-05T20:54:56.637566077Z 2025-01-05T20:54:56.637Z TRACE 1 --- [gateway] [    parallel-11] s.c.g.f.ReactiveLoadBalancerClientFilter : ReactiveLoadBalancerClientFilter url before: lb://product-composite/product-composite/1
2025-01-05T20:54:56.638211676Z 2025-01-05T20:54:56.638Z TRACE 1 --- [gateway] [    parallel-11] s.c.g.f.ReactiveLoadBalancerClientFilter : LoadBalancerClientFilter url chosen: http://1b2ed622e699:8080/product-composite/1
2025-01-05T20:54:56.638582781Z 2025-01-05T20:54:56.638Z DEBUG 1 --- [gateway] [    parallel-11] g.f.h.o.ObservedRequestHttpHeadersFilter : Will instrument the HTTP request headers [Host:"localhost:8443", User-Agent:"HTTPie/2.6.0", Accept-Encoding:"gzip, deflate", Accept:"*/*", Authorization:"Bearer eyJraWQiOiJmMWViOWVjMC1iMzI2LTQ3ODgtYTQ4OC0yMjhiOWNjMmZjYWYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3cml0ZXIiLCJhdWQiOiJ3cml0ZXIiLCJuYmYiOjE3MzYxMTA0NzksInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJwcm9kdWN0OnJlYWQiXSwiaXNzIjoiaHR0cDovL2F1dGgtc2VydmVyOjk5OTkiLCJleHAiOjE3MzYxMTQwNzksImlhdCI6MTczNjExMDQ3OX0.lZmo3pOSbPlojW3KLmW8Fw3tQRH_ZbWaCmLTMipgivF0upjN7txjkKX4nPZfJOZI5CPD6Gxloxd15nteBAg_j-FznE401kkbDZpCZPeNtCP2_pi4rFO8ba5zhQNpwkqVc2Sti4SngrDnRyDa0bdHo7dy4NJzvhrRuIGwiHGB9YLOFzDbAY64cFT8wk7oDXnAuOJY2SA1IvS6f2F1lfoD5oLr9lAQy_m2Wby_mmXRL8sY_6vFwaBcueBQ_1gwWEnO1WfAuZ2MEA7uAPJPuZ7Hsyc4lNT5Q__JcgHznsyjfZuSp-LdOjBP1vKljd7tDTKo6funyumwtJkq2QCRMDVzEA", Forwarded:"proto=https;host="localhost:8443";for="172.21.0.1:64030"", X-Forwarded-For:"172.21.0.1", X-Forwarded-Proto:"https", X-Forwarded-Port:"8443", X-Forwarded-Host:"localhost:8443"]
2025-01-05T20:54:56.638870029Z 2025-01-05T20:54:56.638Z DEBUG 1 --- [gateway] [    parallel-11] g.f.h.o.ObservedRequestHttpHeadersFilter : Client observation  {name=http.client.requests(null), error=null, context=name='http.client.requests', contextualName='null', error='null', lowCardinalityKeyValues=[http.method='GET', http.status_code='UNKNOWN', spring.cloud.gateway.route.id='product-composite', spring.cloud.gateway.route.uri='lb://product-composite'], highCardinalityKeyValues=[http.uri='https://localhost:8443/product-composite/1'], map=[class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=6.3345E-5, duration(nanos)=63345.0, startTimeNanos=724991959887}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@30d117ee'], parentObservation=org.springframework.security.web.server.ObservationWebFilterChainDecorator$PhasedObservation@338858f7} created for the request. New headers are [Host:"localhost:8443", User-Agent:"HTTPie/2.6.0", Accept-Encoding:"gzip, deflate", Accept:"*/*", Authorization:"Bearer eyJraWQiOiJmMWViOWVjMC1iMzI2LTQ3ODgtYTQ4OC0yMjhiOWNjMmZjYWYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ3cml0ZXIiLCJhdWQiOiJ3cml0ZXIiLCJuYmYiOjE3MzYxMTA0NzksInNjb3BlIjpbInByb2R1Y3Q6d3JpdGUiLCJwcm9kdWN0OnJlYWQiXSwiaXNzIjoiaHR0cDovL2F1dGgtc2VydmVyOjk5OTkiLCJleHAiOjE3MzYxMTQwNzksImlhdCI6MTczNjExMDQ3OX0.lZmo3pOSbPlojW3KLmW8Fw3tQRH_ZbWaCmLTMipgivF0upjN7txjkKX4nPZfJOZI5CPD6Gxloxd15nteBAg_j-FznE401kkbDZpCZPeNtCP2_pi4rFO8ba5zhQNpwkqVc2Sti4SngrDnRyDa0bdHo7dy4NJzvhrRuIGwiHGB9YLOFzDbAY64cFT8wk7oDXnAuOJY2SA1IvS6f2F1lfoD5oLr9lAQy_m2Wby_mmXRL8sY_6vFwaBcueBQ_1gwWEnO1WfAuZ2MEA7uAPJPuZ7Hsyc4lNT5Q__JcgHznsyjfZuSp-LdOjBP1vKljd7tDTKo6funyumwtJkq2QCRMDVzEA", Forwarded:"proto=https;host="localhost:8443";for="172.21.0.1:64030"", X-Forwarded-For:"172.21.0.1", X-Forwarded-Proto:"https", X-Forwarded-Port:"8443", X-Forwarded-Host:"localhost:8443"]
2025-01-05T20:54:56.639436615Z 2025-01-05T20:54:56.639Z TRACE 1 --- [gateway] [or-http-epoll-3] o.s.c.gateway.filter.NettyRoutingFilter  : outbound route: a84353c9, inbound: [43407e20-32] 
2025-01-05T20:54:56.666083399Z 2025-01-05T20:54:56.665Z DEBUG 1 --- [gateway] [or-http-epoll-3] .f.h.o.ObservedResponseHttpHeadersFilter : Will instrument the response
2025-01-05T20:54:56.666180239Z 2025-01-05T20:54:56.666Z DEBUG 1 --- [gateway] [or-http-epoll-3] .f.h.o.ObservedResponseHttpHeadersFilter : The response was handled for observation {name=http.client.requests(null), error=null, context=name='http.client.requests', contextualName='null', error='null', lowCardinalityKeyValues=[http.method='GET', http.status_code='UNKNOWN', spring.cloud.gateway.route.id='product-composite', spring.cloud.gateway.route.uri='lb://product-composite'], highCardinalityKeyValues=[http.uri='https://localhost:8443/product-composite/1'], map=[class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.027421405, duration(nanos)=2.7421405E7, startTimeNanos=724991959887}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@30d117ee'], parentObservation=org.springframework.security.web.server.ObservationWebFilterChainDecorator$PhasedObservation@338858f7}
2025-01-05T20:54:56.666467538Z 2025-01-05T20:54:56.666Z TRACE 1 --- [gateway] [or-http-epoll-3] o.s.c.g.filter.NettyWriteResponseFilter  : NettyWriteResponseFilter start inbound: a84353c9, outbound: [43407e20-32] 
2025-01-05T20:54:56.667084920Z 2025-01-05T20:54:56.666Z TRACE 1 --- [gateway] [or-http-epoll-3] o.s.c.g.filter.GatewayMetricsFilter      : spring.cloud.gateway.requests tags: [tag(httpMethod=GET),tag(httpStatusCode=200),tag(outcome=SUCCESSFUL),tag(routeId=product-composite),tag(routeUri=lb://product-composite),tag(status=OK)]
2025-01-05T20:54:56.668207804Z 2025-01-05T20:54:56.668Z TRACE 1 --- [gateway] [r-http-epoll-11] o.s.w.s.adapter.HttpWebHandlerAdapter    : [43407e20-32] Completed 200 OK, headers=[Content-Type:"application/json", Content-Length:"764", Cache-Control:"no-cache, no-store, max-age=0, must-revalidate", Pragma:"no-cache", Expires:"0", X-Content-Type-Options:"nosniff", Strict-Transport-Security:"max-age=31536000 ; includeSubDomains", X-Frame-Options:"DENY", X-XSS-Protection:"0", Referrer-Policy:"no-referrer"]
2025-01-05T20:54:57.437726766Z 
```


### 1. Validate HttpStatusCode is 200

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result 
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 764
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate that the retrieved product id is 1

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s | jq .productId

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .productId
```

Response
```text
1
```

### 3. Validate that there are 3 total recommendations

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s | jq ".recommendations | length"
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN"  | jq ".recommendations | length"
```

Response
```text
3
```

### 4. Validate that there are 3 total reviews

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s | jq ".reviews | length"
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $WRITE_ACCESS_TOKEN"  | jq ".recommendations | length" | jq ".reviews | length"
```

Response
```text
3
```

---

## Verify Behavior for NonExistent ProductId


- [1. Validate 404 (Header)](#1-validate-httpstatuscode-is-404-using-header)
- [2. Validate 404 (Message)](#2-validate-httpstatuscode-is-404-using-message)
- [3. Validate Behavior (Error)](#3-validate-behavior-error-type)
- [4. Validate Behavior (Message)](#4-validate-behavior-error-message)

```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/13
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/13 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 156
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "timestamp": "2025-01-05T19:09:55.99138732Z",
    "path": "/product-composite/13",
    "message": "No product found for productId: 13",
    "status": 404,
    "error": "Not Found"
}
```

### 1. Validate HttpStatusCode is 404 (using header)

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/13 --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/13 "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result
```text
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 157
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate HttpStatusCode is 404 (using message)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/13 -s | jq .status
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/13 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .status
```

Result
```text
404
```

### 3. Validate Behavior (Error Type)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/13 -s | jq .error
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/13 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .error
```

Result
```text
"Not Found"
```


### 4. Validate Behavior (Error Message)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/13 -s | jq .message
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/13 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .message
```

Result
```text
"No product found for productId: 13"
```

---

## Verify Request for Product Without Recommendations

- [1. Validate 200](#1-validate-httpstatuscode-is-200-norec)
- [2. Validate Correct ProductId](#2-validate-that-the-retrieved-product-id-is-113)
- [3. Validate No Recommendations](#3-validate-that-there-are-no-recommendations)
- [4. Validate Number of Reviews](#4-validate-that-there-are-3-total-reviews-norec)


Context of the test

```text
PRODUCT_ID: 113
Recommendations: []
Reviews: 1,2,3
```

The following command gets the entire composite output

```shell
# curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/113
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 13 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 517
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "productId": 113,
    "name": "product name A",
    "weight": 100,
    "recommendations": [],
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
        "compositeAddress": "046d084ab698/172.25.0.10:8080",
        "productAddress": "3467817bf513/172.25.0.8:8080",
        "recommendationAddress": "9adb586447ee/172.25.0.11:8080",
        "reviewAddress": ""
    }
}
```

### 1. Validate HttpStatusCode is 200 (NoRec)

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/113 --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/113 "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 517
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate that the retrieved product id is 113

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/113 -s | jq .productId
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/113 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .productId
```

Response
```text
113
```

### 3. Validate that there are no recommendations

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/113 -s | jq ".recommendations | length"

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/113 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq ".recommendations | length"
```

Response
```text
0
```

### 4. Validate that there are 3 total reviews (NoRec)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/113 -s | jq ".reviews | length"

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/113 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq ".reviews | length"
```
Response
```text
3
```

---

## Verify Request for Product Without Reviews

- [1. Validate 200](#1-validate-httpstatuscode-is-200-norev)
- [2. Validate Correct ProductId](#2-validate-that-the-retrieved-product-id-is-213)
- [3. Validate Number of Recommendations](#3-validate-that-there-are-3-recommendations-norev)
- [4. Validate No Reviews](#4-validate-that-there-are-no-reviews)


Context of the test

```text
PRODUCT_ID: 213
Recommendations: 1,2,3
Reviews: []
```

The following command gets the entire composite output

```shell
# curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/213
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/213 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 501
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "productId": 213,
    "name": "product name B",
    "weight": 200,
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
    "reviews": [],
    "serviceAddress": {
        "compositeAddress": "046d084ab698/172.25.0.10:8080",
        "productAddress": "3467817bf513/172.25.0.8:8080",
        "recommendationAddress": "",
        "reviewAddress": "3488efe82b87/172.25.0.9:8080"
    }
}
```

### 1. Validate HttpStatusCode is 200 (NoRev)

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/213 --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/213 "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 501
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate that the retrieved product id is 213

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/213 -s | jq .productId

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/213 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .productId
```

Response
```text
213
```

### 3. Validate that there are 3 recommendations (NoRev)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/213 -s | jq ".recommendations | length"

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/213 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq ".recommendations | length"
```

Response
```text
3
```

### 4. Validate that there are no reviews

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/213 -s | jq ".reviews | length"

# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/213 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq ".reviews | length"
```
Response
```text
0
```

---


## Verify Behavior for Invalid Product Ids


- [1. Validate 422 (Header)](#1-validate-httpstatuscode-is-422-using-header)
- [2. Validate 422 (Message)](#2-validate-httpstatuscode-is-422-using-message)
- [3. Validate Behavior (Error)](#3-validate-behavior-error-type-422)
- [4. Validate Behavior (Message)](#4-validate-behavior-error-message-422)

```shell
# curl
# curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/-1
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/-1 "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json
Content-Length: 155
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "timestamp": "2025-01-05T19:20:18.085084819Z",
    "path": "/product-composite/-1",
    "message": "Invalid productId: -1",
    "status": 422,
    "error": "Unprocessable Entity"
}
```

### 1. Validate HttpStatusCode is 422 (using header)

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/-1 --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/-1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result
```text
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json
Content-Length: 155
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate HttpStatusCode is 422 (using message)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/-1 -s | jq .status
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/-1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .status
```

Result
```text
422
```

### 3. Validate Behavior (Error Type) (422)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/-1 -s | jq .error
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/-1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .error
```

Result
```text
"Unprocessable Entity"
```


### 4. Validate Behavior (Error Message) (422)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/-1 -s | jq .message
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/-1 "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .message
```

Result
```text
"Invalid productId: -1"
```

---


## Verify Behavior for Nonnumeric Product Ids


- [1. Validate 400 (Header)](#1-validate-httpstatuscode-is-400-using-header)
- [2. Validate 400 (Message)](#2-validate-httpstatuscode-is-400-using-message)
- [3. Validate Behavior (Error)](#3-validate-behavior-error-type-400)
- [4. Validate Behavior (Message)](#4-validate-behavior-error-message-400)

```shell
# curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/invalidProductId
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/invalidProductId "Authorization: Bearer $WRITE_ACCESS_TOKEN"
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 178
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "timestamp": "2025-01-05T19:22:54.567+00:00",
    "path": "/product-composite/invalidProductId",
    "status": 400,
    "error": "Bad Request",
    "requestId": "fb2c6a8f-57",
    "message": "Type mismatch."
}
```

### 1. Validate HttpStatusCode is 400 (using header)

Commands
```shell
## curl
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/invalidProductId --head
## httpie
http --verify=no --unsorted https://localhost:8443/product-composite/invalidProductId "Authorization: Bearer $WRITE_ACCESS_TOKEN" -h
```

Result
```text
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 178
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 2. Validate HttpStatusCode is 400 (using message)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/invalidProductId -s | jq .status
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/invalidProductId "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .status
```

Result
```text
400
```

### 3. Validate Behavior (Error Type) (400)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/invalidProductId -s | jq .error
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/invalidProductId "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .error
```

Result
```text
"Bad Request"
```


### 4. Validate Behavior (Error Message) (400)

Commands
```shell
# curl 
curl -H "Authorization: Bearer $WRITE_ACCESS_TOKEN" -k https://localhost:8443/product-composite/invalidProductId -s | jq .message
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/invalidProductId "Authorization: Bearer $WRITE_ACCESS_TOKEN" | jq .message
```

Result
```text
"Type mismatch."
```

## Verifying 401 Unauthorized

```shell
# curl
curl -k https://localhost:8443/product-composite/1 --head
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 
```

```text
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
content-length: 0
```

---

## Verifying read token constraints

- [1. Validate Read Token](#1-validate-read-token)
- [2. Validate 200 (Read Token)](#2-validate-200-read-token)
- [2. Validate 403 (Forbidden)](#3-validate-403-forbidden)

### 1. Validate Read Token

```shell
# curl
curl -H "Authorization: Bearer $READ_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s | jq
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $READ_ACCESS_TOKEN" 
```

```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 764
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

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
            "recommendationId": 3,
            "author": "author 3",
            "rate": 3,
            "content": "content 3"
        },
        {
            "recommendationId": 2,
            "author": "author 2",
            "rate": 2,
            "content": "content 2"
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
        "compositeAddress": "046d084ab698/172.25.0.10:8080",
        "productAddress": "3467817bf513/172.25.0.8:8080",
        "recommendationAddress": "9adb586447ee/172.25.0.11:8080",
        "reviewAddress": "3488efe82b87/172.25.0.9:8080"
    }
}
```

### 2. Validate 200 (Read Token)

```shell
# curl
curl -H "Authorization: Bearer $READ_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s --head
# httpie
http --verify=no --unsorted https://localhost:8443/product-composite/1 "Authorization: Bearer $READ_ACCESS_TOKEN" -h
```

```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 764
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
```

### 3. Validate 403 (Forbidden)

```shell
# curl
curl -X DELETE -H "Authorization: Bearer $READ_ACCESS_TOKEN" -k https://localhost:8443/product-composite/1 -s --head
# httpie
http --verify=no --unsorted DELETE https://localhost:8443/product-composite/1 "Authorization: Bearer $READ_ACCESS_TOKEN"
```

```text
HTTP/1.1 403 Forbidden
WWW-Authenticate: Bearer error="insufficient_scope", error_description="The request requires higher privileges than provided by the access token.", error_uri="https://tools.ietf.org/html/rfc6750#section-3.1"
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
content-length: 0
```

---

## Verifying access to OpenAPI URLs

- [1. Validate 302](#1-validate-httpstatuscode-302)
- [2. Validate 200 (After Follow)](#2-validate-httpstatuscode-is-200-after-follow)
- [3. Validate 200 (Webjar Endpoint)](#3-validate-httpstatuscode-for-webjar-endpoint)
- [4. Validate 200 (API Docs Endpoint)](#4-validate-httpstatuscode-for-api-docs-endpoint)
- [5. Validate OpenAPI Version](#5-validate-openapi-version)
- [6. Validate server URL](#6-validate-server-url)
- [7. Validate 200 (api-docs.yaml)](#7-validate-httpstatuscode-for-api-docsyaml-endpoint)

### 1. Validate HttpStatusCode 302

Commands
```shell
## curl
curl -ks https://localhost:8443/openapi/swagger-ui.html --head
## httpie
http --verify=no https://localhost:8443/openapi/swagger-ui.html --unsorted
```

Result
```text
HTTP/1.1 302 Found
Location: /openapi/webjars/swagger-ui/index.html
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer
content-length: 0
```

### 2. Validate HttpStatusCode is 200 after Follow

Commands
```shell
## curl
curl -ksL https://localhost:8443/openapi/swagger-ui.html --head
## httpie
http --verify=no -F https://localhost:8443/openapi/swagger-ui.html --unsorted
```


Result (Httpie Shows the 200 and the content, Curl only shows the head)
```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --verify=no -F https://localhost:8443/openapi/swagger-ui.html --unsorted
HTTP/1.1 200 OK
Last-Modified: Tue, 24 Dec 2024 03:37:46 GMT
Content-Length: 734
Content-Type: text/html
Accept-Ranges: bytes
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

<!-- HTML for static distribution bundle build -->
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Swagger UI</title>
    <link rel="stylesheet" type="text/css" href="./swagger-ui.css" />
    <link rel="stylesheet" type="text/css" href="index.css" />
    <link rel="icon" type="image/png" href="./favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="./favicon-16x16.png" sizes="16x16" />
  </head>

  <body>
    <div id="swagger-ui"></div>
    <script src="./swagger-ui-bundle.js" charset="UTF-8"> </script>
    <script src="./swagger-ui-standalone-preset.js" charset="UTF-8"> </script>
    <script src="./swagger-initializer.js" charset="UTF-8"> </script>
  </body>
</html>
```

### 3. Validate HttpStatusCode for webjar Endpoint
Commands
```shell
## curl
curl -ks https://localhost:8443/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config  --head
## httpie
http --verify=no https://localhost:8443/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config --unsorted
```

Result (shows all) 
```text
HTTP/1.1 200 OK
Last-Modified: Tue, 24 Dec 2024 03:37:46 GMT
Content-Length: 734
Content-Type: text/html
Accept-Ranges: bytes
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

<!-- HTML for static distribution bundle build -->
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Swagger UI</title>
    <link rel="stylesheet" type="text/css" href="./swagger-ui.css" />
    <link rel="stylesheet" type="text/css" href="index.css" />
    <link rel="icon" type="image/png" href="./favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="./favicon-16x16.png" sizes="16x16" />
  </head>

  <body>
    <div id="swagger-ui"></div>
    <script src="./swagger-ui-bundle.js" charset="UTF-8"> </script>
    <script src="./swagger-ui-standalone-preset.js" charset="UTF-8"> </script>
    <script src="./swagger-initializer.js" charset="UTF-8"> </script>
  </body>
</html>
```
### 4. Validate HttpStatusCode for api-docs endpoint

(For Httpie this dumps all the OpenAPI documentation)

Commands
```shell
## curl
curl -ks https://localhost:8443/openapi/v3/api-docs --head
## httpie
http --verify=no https://localhost:8443/openapi/v3/api-docs --unsorted
```

Results
```text
(base) ~/IdeaProjects/ServiceTransformation git:[develop]
http --verify=no https://localhost:8443/openapi/v3/api-docs --unsorted

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 5516
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

{
    "openapi": "3.0.1",
    "info": {
        "title": "Product Composite API",
        "description": "Aggregation of Product, Recommendation, Review Microservices",
        "termsOfService": "Not Applicable",
        "contact": {
            "name": "Ed Mangini",
            "url": "https://emangini.com",
            "email": "me@emangini.com"
        },
        "license": {
            "name": "MIT License",
            "url": "https://opensource.org/license/mit/"
        }
    },
    "externalDocs": {
        "description": "GitHub Page",
        "url": "https://github.com/edtbl76/ServiceTransformation/blob/main/README.md"
    },
    "servers": [
        {
            "url": "https://localhost:8443",
            "description": "Generated server url"
        }
    ],
    "tags": [
        {
            "name": "ProductComposite",
            "description": "REST API for composite product information."
        }
    ],
    "paths": {
        "/product-composite": {
            "post": {
                "tags": [
                    "ProductComposite"
                ],
                "summary": "${api.product-composite.create-composite-product.description}",
                "description": "${api.product-composite.create-composite-product.notes}",
                "operationId": "createProduct",
                "requestBody": {
                    "content": {
                        "application/json": {
                            "schema": {
                                "$ref": "#/components/schemas/ProductAggregate"
                            }
                        }
                    },
                    "required": true
                },
                "responses": {
                    "400": {
                        "description": "Bad Request, invalid format of the request. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "404": {
                        "description": "Not Found",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "422": {
                        "description": "Unprocessable entity, input parameters caused the processing to fail. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "202": {
                        "description": "Accepted"
                    }
                },
                "security": [
                    {
                        "security_auth": []
                    }
                ]
            }
        },
        "/product-composite/{productId}": {
            "get": {
                "tags": [
                    "ProductComposite"
                ],
                "summary": "Returns a composite view of the specified product id",
                "description": "# Normal response\nIf the requested product id is found the method will return information regarding:\n1. Product information\n1. Reviews\n1. Recommendations\n1. Service Addresses\\n(technical information regarding the addresses of the microservices that created the response)\n\n# Expected partial and error responses\n1. If no product information is found, a **404 - Not Found** error will be returned\n1. If no recommendations or reviews are found for a product, a partial response will be returned\n",
                "operationId": "getProduct",
                "parameters": [
                    {
                        "name": "productId",
                        "in": "path",
                        "required": true,
                        "schema": {
                            "type": "integer",
                            "format": "int32"
                        }
                    }
                ],
                "responses": {
                    "400": {
                        "description": "Bad Request, invalid format of the request. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "404": {
                        "description": "Not found, the specified id does not exist",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "422": {
                        "description": "Unprocessable entity, input parameters caused the processing to fail. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "200": {
                        "description": "OK",
                        "content": {
                            "application/json": {
                                "schema": {
                                    "$ref": "#/components/schemas/ProductAggregate"
                                }
                            }
                        }
                    }
                },
                "security": [
                    {
                        "security_auth": []
                    }
                ]
            },
            "delete": {
                "tags": [
                    "ProductComposite"
                ],
                "summary": "${api.product-composite.delete-composite-product.description}",
                "description": "${api.product-composite.delete-composite-product.notes}",
                "operationId": "deleteProduct",
                "parameters": [
                    {
                        "name": "productId",
                        "in": "path",
                        "required": true,
                        "schema": {
                            "type": "integer",
                            "format": "int32"
                        }
                    }
                ],
                "responses": {
                    "400": {
                        "description": "Bad Request, invalid format of the request. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "404": {
                        "description": "Not Found",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "422": {
                        "description": "Unprocessable entity, input parameters caused the processing to fail. See response message for more information",
                        "content": {
                            "*/*": {
                                "schema": {
                                    "$ref": "#/components/schemas/HttpErrorInfo"
                                }
                            }
                        }
                    },
                    "202": {
                        "description": "Accepted"
                    }
                },
                "security": [
                    {
                        "security_auth": []
                    }
                ]
            }
        }
    },
    "components": {
        "schemas": {
            "HttpErrorInfo": {
                "type": "object",
                "properties": {
                    "timestamp": {
                        "type": "string",
                        "format": "date-time"
                    },
                    "path": {
                        "type": "string"
                    },
                    "message": {
                        "type": "string"
                    },
                    "status": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "error": {
                        "type": "string"
                    }
                }
            },
            "ProductAggregate": {
                "type": "object",
                "properties": {
                    "productId": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "name": {
                        "type": "string"
                    },
                    "weight": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "recommendations": {
                        "type": "array",
                        "items": {
                            "$ref": "#/components/schemas/RecommendationSummary"
                        }
                    },
                    "reviews": {
                        "type": "array",
                        "items": {
                            "$ref": "#/components/schemas/ReviewSummary"
                        }
                    },
                    "serviceAddress": {
                        "$ref": "#/components/schemas/ServiceAddresses"
                    }
                }
            },
            "RecommendationSummary": {
                "type": "object",
                "properties": {
                    "recommendationId": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "author": {
                        "type": "string"
                    },
                    "rate": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "content": {
                        "type": "string"
                    }
                }
            },
            "ReviewSummary": {
                "type": "object",
                "properties": {
                    "reviewId": {
                        "type": "integer",
                        "format": "int32"
                    },
                    "author": {
                        "type": "string"
                    },
                    "subject": {
                        "type": "string"
                    },
                    "content": {
                        "type": "string"
                    }
                }
            },
            "ServiceAddresses": {
                "type": "object",
                "properties": {
                    "compositeAddress": {
                        "type": "string"
                    },
                    "productAddress": {
                        "type": "string"
                    },
                    "recommendationAddress": {
                        "type": "string"
                    },
                    "reviewAddress": {
                        "type": "string"
                    }
                }
            }
        },
        "securitySchemes": {
            "security-auth": {
                "type": "oauth2",
                "flows": {
                    "authorizationCode": {
                        "authorizationUrl": "https://localhost:8443/oauth2/authorize",
                        "tokenUrl": "https://localhost:8443/oauth2/token",
                        "scopes": {
                            "product:read": "read scope",
                            "product:write": "write scope"
                        }
                    }
                }
            }
        }
    }
}
```

### 5. Validate OpenAPI Version

(For Httpie this dumps all the OpenAPI documentation)

Commands
```shell
## curl
curl -ks https://localhost:8443/openapi/v3/api-docs | jq .openapi
## httpie
http --verify=no https://localhost:8443/openapi/v3/api-docs --unsorted | jq .openapi
```

Results
```text
"3.0.1"
```

### 6. Validate Server URL

```shell
## curl
curl -ks https://localhost:8443/openapi/v3/api-docs | jq -r .servers[].url
## httpie
http --verify=no https://localhost:8443/openapi/v3/api-docs --unsorted | jq -r .servers[].url
```

```text
https://localhost:8443
```

### 7. Validate HttpStatusCode for api-docs.yaml endpoint

(For Httpie this dumps all the OpenAPI documentation)

Commands
```shell
## curl
curl -ks https://localhost:8443/openapi/v3/api-docs.yaml --head
## httpie
http --verify=no https://localhost:8443/openapi/v3/api-docs.yaml --unsorted
```

Results
```text
HTTP/1.1 200 OK
Content-Type: application/vnd.oai.openapi
Content-Length: 6919
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
X-Frame-Options: DENY
X-XSS-Protection: 0
Referrer-Policy: no-referrer

openapi: 3.0.1
info:
  title: Product Composite API
  description: "Aggregation of Product, Recommendation, Review Microservices"
  termsOfService: Not Applicable
  contact:
    name: Ed Mangini
    url: https://emangini.com
    email: me@emangini.com
  license:
    name: MIT License
    url: https://opensource.org/license/mit/
externalDocs:
  description: GitHub Page
  url: https://github.com/edtbl76/ServiceTransformation/blob/main/README.md
servers:
- url: https://localhost:8443
  description: Generated server url
tags:
- name: ProductComposite
  description: REST API for composite product information.
paths:
  /product-composite:
    post:
      tags:
      - ProductComposite
      summary: "${api.product-composite.create-composite-product.description}"
      description: "${api.product-composite.create-composite-product.notes}"
      operationId: createProduct
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/ProductAggregate"
        required: true
      responses:
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "404":
          description: Not Found
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "422":
          description: "Unprocessable entity, input parameters caused the processing\
            \ to fail. See response message for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "202":
          description: Accepted
      security:
      - security_auth: []
  /product-composite/{productId}:
    get:
      tags:
      - ProductComposite
      summary: Returns a composite view of the specified product id
      description: |
        # Normal response
        If the requested product id is found the method will return information regarding:
        1. Product information
        1. Reviews
        1. Recommendations
        1. Service Addresses\n(technical information regarding the addresses of the microservices that created the response)

        # Expected partial and error responses
        1. If no product information is found, a **404 - Not Found** error will be returned
        1. If no recommendations or reviews are found for a product, a partial response will be returned
      operationId: getProduct
      parameters:
      - name: productId
        in: path
        required: true
        schema:
          type: integer
          format: int32
      responses:
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "404":
          description: "Not found, the specified id does not exist"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "422":
          description: "Unprocessable entity, input parameters caused the processing\
            \ to fail. See response message for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ProductAggregate"
      security:
      - security_auth: []
    delete:
      tags:
      - ProductComposite
      summary: "${api.product-composite.delete-composite-product.description}"
      description: "${api.product-composite.delete-composite-product.notes}"
      operationId: deleteProduct
      parameters:
      - name: productId
        in: path
        required: true
        schema:
          type: integer
          format: int32
      responses:
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "404":
          description: Not Found
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "422":
          description: "Unprocessable entity, input parameters caused the processing\
            \ to fail. See response message for more information"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "202":
          description: Accepted
      security:
      - security_auth: []
components:
  schemas:
    HttpErrorInfo:
      type: object
      properties:
        timestamp:
          type: string
          format: date-time
        path:
          type: string
        message:
          type: string
        status:
          type: integer
          format: int32
        error:
          type: string
    ProductAggregate:
      type: object
      properties:
        productId:
          type: integer
          format: int32
        name:
          type: string
        weight:
          type: integer
          format: int32
        recommendations:
          type: array
          items:
            $ref: "#/components/schemas/RecommendationSummary"
        reviews:
          type: array
          items:
            $ref: "#/components/schemas/ReviewSummary"
        serviceAddress:
          $ref: "#/components/schemas/ServiceAddresses"
    RecommendationSummary:
      type: object
      properties:
        recommendationId:
          type: integer
          format: int32
        author:
          type: string
        rate:
          type: integer
          format: int32
        content:
          type: string
    ReviewSummary:
      type: object
      properties:
        reviewId:
          type: integer
          format: int32
        author:
          type: string
        subject:
          type: string
        content:
          type: string
    ServiceAddresses:
      type: object
      properties:
        compositeAddress:
          type: string
        productAddress:
          type: string
        recommendationAddress:
          type: string
        reviewAddress:
          type: string
  securitySchemes:
    security-auth:
      type: oauth2
      flows:
        authorizationCode:
          authorizationUrl: https://localhost:8443/oauth2/authorize
          tokenUrl: https://localhost:8443/oauth2/token
          scopes:
            product:read: read scope
            product:write: write scope
```
---

## Shutting Down Test Environment

```shell
docker-compose down  --remove-orphans
unset WRITE_ACCESS_TOKEN
unset READ_ACCESS_TOKEN
echo $WRITE_ACCESS_TOKEN
echo $READ_ACCESS_TOKEN
```

```text
[+] Running 11/11
 ✔ Container servicetransformation-eureka-1             Removed                                                                                                                                                                                                                                             0.2s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                            10.5s 
 ✔ Container servicetransformation-gateway-1            Removed                                                                                                                                                                                                                                            10.5s 
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                            10.4s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                            10.4s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                            10.3s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                             0.3s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                             1.4s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                             1.3s 
 ✔ Container servicetransformation-auth-server-1        Removed                                                                                                                                                                                                                                             3.2s 
 ✔ Network servicetransformation_default                Removed                                                                                                                                                                                                                                             0.1s 
```