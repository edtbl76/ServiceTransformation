# Testing with Test Runner

- [Starting the test environment](#start-the-environment)
- [Checking Liveness w/ Spring Actuator](#spring-actuator)
- [Verifying a Normal Request](#verifying-a-normal-request)
- [Verifying Behavior for Nonexistent Product](#verify-behavior-for-nonexistent-productid)
- [Verifying Request for Product w/o Recommendations](#verify-request-for-product-without-recommendations)
- [Verifying Request for Product w/o Reviews](#verify-request-for-product-without-reviews)
- [Verifying Behavior for Invalid Product Ids](#verify-behavior-for-invalid-product-ids)
- [Verifying Behavior for Non-Numeric Product Ids](#verify-behavior-for-nonnumeric-product-ids)
- [Verifying Access to OpenAPI Urls](#verifying-access-to-openapi-urls)
- [Shutting down the test environment](#shutting-down-test-environment)

---

## Start the environment

To start the test environment and seed data w/o stopping containers: 
```shell
./testRunner.sh start
```

---

## Spring Actuator

The healthcheck ensures that the the Spring Services are up. 

NOTE: Links to Spring Actuator endpoints can be found in the [README](../README.md).

- [1. curl healthcheck](#1-curl-healthcheck)
- [2. httpie healthcheck](#2-httpie-healthcheck)

### 1. Curl HealthCheck

```shell
curl http://localhost:8080/actuator/health -s | jq
```
```text
{
  "status": "UP",
  "components": {
    "binders": {
      "status": "UP",
      "components": {
        "rabbit": {
          "status": "UP",
          "details": {
            "version": "4.0.5"
          }
        }
      }
    },
    "coreServices": {
      "status": "UP",
      "components": {
        "product": {
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
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 101129359360,
        "free": 45118107648,
        "threshold": 10485760,
        "path": "/application/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    },
    "rabbit": {
      "status": "UP",
      "details": {
        "version": "4.0.5"
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

### 2. httpie healthcheck

```shell
http http://localhost:8080/actuator/health --unsorted
```
```text
HTTP/1.1 200 OK
Content-Type: application/vnd.spring-boot.actuator.v3+json
Content-Length: 543

{
    "status": "UP",
    "components": {
        "binders": {
            "status": "UP",
            "components": {
                "rabbit": {
                    "status": "UP",
                    "details": {
                        "version": "4.0.5"
                    }
                }
            }
        },
        "coreServices": {
            "status": "UP",
            "components": {
                "product": {
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
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 101129359360,
                "free": 45117939712,
                "threshold": 10485760,
                "path": "/application/.",
                "exists": true
            }
        },
        "ping": {
            "status": "UP"
        },
        "rabbit": {
            "status": "UP",
            "details": {
                "version": "4.0.5"
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
http :8080/product-composite/1 --unsorted

```
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 762

{
    "productId": 1,
    "name": "product name C",
    "weight": 300,
    "recommendations": [
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
        },
        {
            "recommendationId": 1,
            "author": "author 1",
            "rate": 1,
            "content": "content 1"
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
        "compositeAddress": "dee156c5a205/172.30.0.2:8080",
        "productAddress": "bf6e4dd3c913/172.30.0.7:8080",
        "recommendationAddress": "baff99b64eac/172.30.0.8:8080",
        "reviewAddress": "0b774f07666b/172.30.0.6:8080"
    }
}
```

### 1. Validate HttpStatusCode is 200

Commands
```shell
## curl
curl http://localhost:8080/product-composite/1 --head
## httpie
http :8080/product-composite/1 -h
```

Result 
```text
HTTP/1.1 200 OK
Content-Length: 762
Content-Type: application/json
```

### 2. Validate that the retrieved product id is 1

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/1 -s | jq .productId
# httpie
http :8080/product-composite/1 --unsorted | jq .productId
```

Response
```text
1
```

### 3. Validate that there are 3 total recommendations

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/1 -s | jq ".recommendations | length"
# httpie
http :8080/product-composite/1 --unsorted | jq ".recommendations | length"
```

Response
```text
3
```

### 4. Validate that there are 3 total reviews

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/1 -s | jq ".reviews | length"
# httpie
http :8080/product-composite/1 --unsorted | jq ".reviews | length"
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
# curl
curl http://localhost:8080/product-composite/13 -s | jq
# httpie
http :8080/product-composite/13 --unsorted
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 157

{
    "timestamp": "2025-01-01T22:58:00.360965001Z",
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
curl http://localhost:8080/product-composite/13 --head
## httpie
http :8080/product-composite/13 -h
```

Result
```text
HTTP/1.1 404 Not Found
Content-Type: application/json
Content-Length: 157
```

### 2. Validate HttpStatusCode is 404 (using message)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/13 -s | jq .status
## httpie
http :8080/product-composite/13 --unsorted | jq .status
```

Result
```text
404
```

### 3. Validate Behavior (Error Type)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/13 -s | jq .error
## httpie
http :8080/product-composite/13 --unsorted | jq .error
```

Result
```text
"Not Found"
```


### 4. Validate Behavior (Error Message)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/13 -s | jq .message
## httpie
http :8080/product-composite/13 --unsorted | jq .message
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
http :8080/product-composite/113 --unsorted
```
```text
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 515

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
        "compositeAddress": "dee156c5a205/172.30.0.2:8080",
        "productAddress": "bf6e4dd3c913/172.30.0.7:8080",
        "recommendationAddress": "baff99b64eac/172.30.0.8:8080",
        "reviewAddress": ""
    }
}
```

### 1. Validate HttpStatusCode is 200 (NoRec)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/113 --head
## httpie
http :8080/product-composite/113 -h
```

Result
```text
HTTP/1.1 200 OK
Content-Length: 515
Content-Type: application/json
```

### 2. Validate that the retrieved product id is 113

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/113 -s | jq .productId
# httpie
http :8080/product-composite/113 --unsorted | jq .productId
```

Response
```text
113
```

### 3. Validate that there are no recommendations

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/113 -s | jq ".recommendations | length"
# httpie
http :8080/product-composite/113 --unsorted | jq ".recommendations | length"
```

Response
```text
0
```

### 4. Validate that there are 3 total reviews (NoRec)

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/113 -s | jq ".reviews | length"
# httpie
http :8080/product-composite/113 --unsorted | jq ".reviews | length"
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
http :8080/product-composite/213 --unsorted
```
```text
(base) ~/IdeaProjects/ServiceTransformation/doc git:[develop]
http :8080/product-composite/213 --unsorted
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 500

{
    "productId": 213,
    "name": "product name B",
    "weight": 200,
    "recommendations": [
        {
            "recommendationId": 3,
            "author": "author 3",
            "rate": 3,
            "content": "content 3"
        },
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
        }
    ],
    "reviews": [],
    "serviceAddress": {
        "compositeAddress": "dee156c5a205/172.30.0.2:8080",
        "productAddress": "bf6e4dd3c913/172.30.0.7:8080",
        "recommendationAddress": "",
        "reviewAddress": "0b774f07666b/172.30.0.6:8080"
    }
}
}
```

### 1. Validate HttpStatusCode is 200 (NoRev)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/213 --head
## httpie
http :8080/product-composite/213 -h
```

Result
```text
HTTP/1.1 200 OK
Content-Length: 500
Content-Type: application/json
```

### 2. Validate that the retrieved product id is 213

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/213 -s | jq .productId
# httpie
http :8080/product-composite/213 --unsorted | jq .productId
```

Response
```text
213
```

### 3. Validate that there are 3 recommendations (NoRev)

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/213 -s | jq ".recommendations | length"
# httpie
http :8080/product-composite/213 --unsorted | jq ".recommendations | length"
```

Response
```text
3
```

### 4. Validate that there are no reviews

Commands
```shell
# curl 
curl http://localhost:8080/product-composite/213 -s | jq ".reviews | length"
# httpie
http :8080/product-composite/213 --unsorted | jq ".reviews | length"
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
curl http://localhost:8080/product-composite/-1 -s | jq
# httpie
http :8080/product-composite/-1 --unsorted
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json
Content-Length: 155

{
    "timestamp": "2025-01-01T23:34:29.580728876Z",
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
curl http://localhost:8080/product-composite/-1 --head
## httpie
http :8080/product-composite/-1 -h
```

Result
```text
HTTP/1.1 422 Unprocessable Entity
Content-Length: 155
Content-Type: application/json
```

### 2. Validate HttpStatusCode is 422 (using message)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/-1 -s | jq .status
## httpie
http :8080/product-composite/-1 --unsorted | jq .status
```

Result
```text
422
```

### 3. Validate Behavior (Error Type) (422)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/-1 -s | jq .error
## httpie
http :8080/product-composite/-1 --unsorted | jq .error
```

Result
```text
"Unprocessable Entity"
```


### 4. Validate Behavior (Error Message) (422)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/-1 -s | jq .message
## httpie
http :8080/product-composite/-1 --unsorted | jq .message
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
curl http://localhost:8080/product-composite/invalidProductId -s | jq
# httpie
http :8080/product-composite/invalidProductId --unsorted
```

NOTE: The output is from the httpie command (Curl doesn't show the header by default)
```text
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: 179

{
    "timestamp": "2025-01-01T23:41:27.559+00:00",
    "path": "/product-composite/invalidProductId",
    "status": 400,
    "error": "Bad Request",
    "requestId": "d6ab37c0-114",
    "message": "Type mismatch."
}
```

### 1. Validate HttpStatusCode is 400 (using header)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/invalidProductId --head
## httpie
http :8080/product-composite/invalidProductId -h
```

Result
```text
HTTP/1.1 400 Bad Request
Content-Length: 179
Content-Type: application/json
```

### 2. Validate HttpStatusCode is 400 (using message)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/invalidProductId -s | jq .status
## httpie
http :8080/product-composite/invalidProductId --unsorted | jq .status
```

Result
```text
400
```

### 3. Validate Behavior (Error Type) (400)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/invalidProductId -s | jq .error
## httpie
http :8080/product-composite/invalidProductId--unsorted | jq .error
```

Result
```text
"Bad Request"
```


### 4. Validate Behavior (Error Message) (400)

Commands
```shell
## curl
curl http://localhost:8080/product-composite/invalidProductId -s | jq .message
## httpie
http :8080/product-composite/invalidProductId --unsorted | jq .message
```

Result
```text
"Type mismatch."
```

---

## Verifying access to OpenAPI URLs

- [1. Validate 302](#1-validate-httpstatuscode-302)
- [2. Validate 200 (After Follow)](#2-validate-httpstatuscode-is-200-after-follow)
- [3. Validate 200 (Webjar Endpoint)](#3-validate-httpstatuscode-for-webjar-endpoint)
- [4. Validate 200 (API Docs Endpoint)](#4-validate-httpstatuscode-for-api-docs-endpoint)
- [5. Validate OpenAPI Version](#5-validate-openapi-version)
- [6. Validate 200 (api-docs.yaml)](#6-validate-httpstatuscode-for-api-docsyaml-endpoint)

### 1. Validate HttpStatusCode 302

Commands
```shell
## curl
curl http://localhost:8080/openapi/swagger-ui.html -s --head
## httpie
http :8080/openapi/swagger-ui.html --unsorted
```

Result
```text
HTTP/1.1 302 Found
Location: /openapi/webjars/swagger-ui/index.html
content-length: 0
```

### 2. Validate HttpStatusCode is 200 after Follow

Commands
```shell
## curl
curl http://localhost:8080/openapi/swagger-ui.html -sL --head
## httpie
http :8080/openapi/swagger-ui.html -F --unsorted
```

Result (Httpie Shows the 200 and the content, Curl only shows the head)
```text
HTTP/1.1 200 OK
Last-Modified: Tue, 24 Dec 2024 03:37:46 GMT
Content-Length: 734
Content-Type: text/html
Accept-Ranges: bytes

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
curl http://localhost:8080/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config -s --head
## httpie
http :8080/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config --unsorted
```

Result (shows all) 
```text
## curl
HTTP/1.1 200 OK
Last-Modified: Tue, 24 Dec 2024 03:37:46 GMT
Content-Length: 734
Content-Type: text/html
Accept-Ranges: bytes

## httpie
HTTP/1.1 200 OK
Last-Modified: Tue, 24 Dec 2024 03:37:46 GMT
Content-Length: 734
Content-Type: text/html
Accept-Ranges: bytes

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
curl http://localhost:8080/openapi/v3/api-docs -s --head
## httpie
http :8080/openapi/v3/api-docs --unsorted
```

Results
```text
## curl
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 5148

#httpie
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 5148

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
            "url": "http://localhost:8080",
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
                }
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
                }
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
                }
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
        }
    }
}
```

### 5. Validate OpenAPI Version

(For Httpie this dumps all the OpenAPI documentation)

Commands
```shell
## curl
curl http://localhost:8080/openapi/v3/api-docs -s | jq .openapi
## httpie
http :8080/openapi/v3/api-docs --unsorted | jq .openapi
```

Results
```text
"3.0.1"
```

### 6. Validate HttpStatusCode for api-docs.yaml endpoint

(For Httpie this dumps all the OpenAPI documentation)

Commands
```shell
## curl
curl http://localhost:8080/openapi/v3/api-docs.yaml -s --head
## httpie
http :8080/openapi/v3/api-docs.yaml --unsorted
```

Results
```text
## curl
HTTP/1.1 200 OK
Content-Type: application/vnd.oai.openapi
Content-Length: 6477

##httpie
HTTP/1.1 200 OK
Content-Type: application/vnd.oai.openapi
Content-Length: 6477

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
- url: http://localhost:8080
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
        "404":
          description: Not Found
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
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
        "404":
          description: "Not found, the specified id does not exist"
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
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
        "404":
          description: Not Found
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/HttpErrorInfo"
        "400":
          description: "Bad Request, invalid format of the request. See response message\
            \ for more information"
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
```
---

## Shutting Down Test Environment

```shell
docker-compose down
```

```text
[+] Running 8/7
 ✔ Container servicetransformation-recommendation-1     Removed                                                                                                                                                                                                                                             4.6s 
 ✔ Container servicetransformation-product-1            Removed                                                                                                                                                                                                                                             4.5s 
 ✔ Container servicetransformation-product-composite-1  Removed                                                                                                                                                                                                                                             4.4s 
 ✔ Container servicetransformation-review-1             Removed                                                                                                                                                                                                                                             2.6s 
 ✔ Container servicetransformation-mysql-1              Removed                                                                                                                                                                                                                                             0.6s 
 ✔ Container servicetransformation-mongodb-1            Removed                                                                                                                                                                                                                                             0.2s 
 ✔ Container servicetransformation-rabbitmq-1           Removed                                                                                                                                                                                                                                             1.2s 
 ✔ Network servicetransformation_default                Removed     
```