#!/usr/bin/env bash
#
# Usage:
#
#   HOST=localhost PORT=8443 ./testRunner.sh
#
: ${HOST=localhost}
: ${PORT=8443}
: ${PRODUCT_ID_OK=1}
: ${PRODUCT_ID_NOT_FOUND=13}
: ${PRODUCT_ID_NO_RECOMMENDATIONS=113}
: ${PRODUCT_ID_NO_REVIEWS=213}

function assertCurl() {

  local expectedHttpStatus=$1
  local curlCommand="$2 -w \"%{http_code}\""
  local result=$(eval $curlCommand)
  local httpStatus="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpStatus" = "$expectedHttpStatus" ]
  then
    if [ "$httpStatus" = "200" ]
    then
      echo "Test OK (HTTP Status: $httpStatus)"
    else
      echo "Test OK (HTTP Status: $httpStatus, $RESPONSE)"
    fi
  else
    echo "Test FAILED, EXPECTED HTTP Status: $expectedHttpStatus, GOT: $httpStatus, ABORTING!"
    echo "- Failing Command: $curlCommand"
    echo "- Response Body: $RESPONSE"
    exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, ABORTING!"
    exit 1
  fi
}

function testUrl() {
  url=$@
  if curl $url -ks -f -o /dev/null
  then
    return 0
  else
    return 1
  fi;
}

function waitForService() {
  url=$@
  echo -n "Wait for: $url... "
  n=0
  until testUrl $url
  do
    n=$((n + 1))
    if [[ $n == 100 ]]
    then
      echo "Give up"
      exit 1
    else
      sleep 3
      echo -n ", retry #$n "
    fi
  done
  echo "DONE, continues..."
}

function testCompositeCreated() {

  # Expects Product Composite for productId has been created w/ 3 recommendations and reviews
  if ! assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_OK -s"
  then
    echo -n "FAIL"
    return 1
  fi

  set +e
  assertEqual "$PRODUCT_ID_OK" $(echo $RESPONSE | jq .productId)
  if [ "$?" -eq "1" ] ; then return 1; fi

  assertEqual 3  $(echo $RESPONSE | jq ".recommendations | length")
  if [ "$?" -eq "1" ] ; then return 1; fi

  assertEqual 3  $(echo $RESPONSE | jq ".reviews | length")
  if [ "$?" -eq "1" ] ; then return 1; fi

}

function waitForMessageProcessing() {

  echo "Waiting for messages to be processed..."

  # Give some time to complete
  sleep 1

  n=0
  until testCompositeCreated
  do
    n=$((n + 1))
    if [[ $n == 40 ]]
    then
      echo " Give up"
      exit 1
    else
      sleep 6
      echo -n ", retry #$n "
    fi
  done

  echo "All messages have been processed"
}

function recreateComposite() {
  local productId=$1
  local composite=$2

  assertCurl 202 "curl -X DELETE $AUTH -k https://$HOST:$PORT/product-composite/${productId} -s"
  assertEqual 202 $(curl -X POST -s -k https://$HOST:$PORT/product-composite -H "Content-Type: application/json" -H "Authorization: Bearer $ACCESS_TOKEN" --data "$composite" -w "%{http_code}")
}

function seedTestData() {
  body="{\"productId\":$PRODUCT_ID_NO_RECOMMENDATIONS"
  body+=\
',"name":"product name A","weight":100, "reviews":[
{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
  recreateComposite "$PRODUCT_ID_NO_RECOMMENDATIONS" "$body"

  body="{\"productId\":$PRODUCT_ID_NO_REVIEWS"
  body+=\
',"name":"product name B","weight":200, "recommendations":[
{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
]}'
  recreateComposite "$PRODUCT_ID_NO_REVIEWS" "$body"

  body="{\"productId\":$PRODUCT_ID_OK"
  body+=\
',"name":"product name C","weight":300, "recommendations":[
{"recommendationId":1,"author":"author 1","rate":1,"content":"content 1"},
{"recommendationId":2,"author":"author 2","rate":2,"content":"content 2"},
{"recommendationId":3,"author":"author 3","rate":3,"content":"content 3"}
], "reviews":[
{"reviewId":1,"author":"author 1","subject":"subject 1","content":"content 1"},
{"reviewId":2,"author":"author 2","subject":"subject 2","content":"content 2"},
{"reviewId":3,"author":"author 3","subject":"subject 3","content":"content 3"}
]}'
  recreateComposite "$PRODUCT_ID_OK" "$body"
}

set -e

echo "Starting Landscape Tests: " `date`

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
  echo "Restarting test environment..."
  echo "$ docker-compose down --remove-orphans"
  docker-compose down --remove-orphans
  echo "$ docker-compose up -d"
  docker-compose up -d
fi

waitForService curl -k https://$HOST:${PORT}/actuator/health

## Auth 0 Writer Test
export TENANT=dev-k26mww20c882irv6.us.auth0.com
export WRITER_CLIENT_ID=GmgDJIN8YIuIpb2Y7iyUyM1AIz3kiAp1
export WRITER_CLIENT_SECRET=6DqV0ksN_Zi4D8saVhlostqaSm0h0Sg9yc2HUcE1cqqVo6xQhTWBJD4hNUJms41I

# Spring Authorization Server Token
#ACCESS_TOKEN=$(curl -k https://writer:writer@$HOST:$PORT/oauth2/token -d grant_type=client_credentials -s -d scope="product:write product:read" |  jq .access_token -r)

# Auth0 Token
ACCESS_TOKEN=$(curl -X POST https://${TENANT}/oauth/token \
-d grant_type=client_credentials \
-d audience=https://localhost:8443/product-composite \
-d scope=product:read+product:write \
-d client_id=$WRITER_CLIENT_ID \
-d client_secret=$WRITER_CLIENT_SECRET -s | jq -r .access_token)

echo ACCESS_TOKEN=$ACCESS_TOKEN
AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""

# Verify Eureka/Service Discovery (All Microservices should be registered)
assertCurl 200 "curl -H "accept:application/json" -k https://username:password@$HOST:$PORT/eureka/api/apps -s"
assertEqual 6 $(echo $RESPONSE | jq ".applications.application | length")

# Verify access to config server and that encrypt/decrypt endpoints work
assertCurl 200 "curl -H "accept:application/json" -k https://username:password@$HOST:$PORT/config/product/docker -s"
TEST_VALUE="hello world"
ENCRYPTED_VALUE=$(curl -k https://username:password@$HOST:$PORT/config/encrypt --data-urlencode "$TEST_VALUE" -s)
DECRYPTED_VALUE=$(curl -k https://username:password@$HOST:$PORT/config/decrypt -d $ENCRYPTED_VALUE -s)
assertEqual "$TEST_VALUE" "$DECRYPTED_VALUE"

echo "Writing test data..."

seedTestData

waitForMessageProcessing

# Verify normal request. (3 recommendations, 3 reviews)
assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_OK -s"
assertEqual $PRODUCT_ID_OK $(echo $RESPONSE | jq .productId)
assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")

# Verify 404 Not Found is returned for non-existing products
assertCurl 404 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_NOT_FOUND -s"
assertEqual "No product found for productId: $PRODUCT_ID_NOT_FOUND" "$(echo $RESPONSE | jq -r .message)"

# Verify no recommendations are returned
assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_NO_RECOMMENDATIONS -s"
assertEqual $PRODUCT_ID_NO_RECOMMENDATIONS $(echo $RESPONSE | jq .productId)
assertEqual 0 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")

# Verify no reviews are returned
assertCurl 200 "curl $AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_NO_REVIEWS -s"
assertEqual $PRODUCT_ID_NO_REVIEWS $(echo $RESPONSE | jq .productId)
assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 0 $(echo $RESPONSE | jq ".reviews | length")

# Verify 422 (Unprocessable Entity) returned for a product Id that is out of range
assertCurl 422 "curl $AUTH -k https://$HOST:$PORT/product-composite/-1 -s"
assertEqual "\"Invalid productId: -1\"" "$(echo $RESPONSE | jq .message)"

# Verify 400 (Bad Request) returned for a product that isn't a number
assertCurl 400 "curl $AUTH -k https://$HOST:$PORT/product-composite/invalidProductId -s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"

# Verify that a request w/o an access token fails as 401 Unauthorized
assertCurl 401 "curl -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_OK -s"

# Setup Reader
export READER_CLIENT_ID=ykQSB1MQqRMGVwmE9gbWjuGJuSvWTM9Q
export READER_CLIENT_SECRET=1qjsvE8YPdXoo1HJ-wb9mhhXwvxzb1Ke4v-ZojQCZ0hYSx0Qx7VO4zRt3mfYA70U

# Spring Authorization Server Access Token
#READER_ACCESS_TOKEN=$(curl -k https://reader:reader@$HOST:$PORT/oauth2/token -d grant_type=client_credentials -d scope="product:read" -s | jq .access_token -r)

# Auth0 Access Token
READER_ACCESS_TOKEN=$(curl -X POST https://$TENANT/oauth/token \
-d grant_type=client_credentials \
-d audience=https://localhost:8443/product-composite \
-d scope=product:read \
-d client_id=$READER_CLIENT_ID \
-d client_secret=$READER_CLIENT_SECRET -s | jq -r .access_token)

echo READER_ACCESS_TOKEN=$READER_ACCESS_TOKEN
READER_AUTH="-H \"Authorization: Bearer $READER_ACCESS_TOKEN\""

# Verify that reader client can call read, but not delete

assertCurl 200 "curl $READER_AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_OK -s"
assertCurl 403 "curl -X DELETE $READER_AUTH -k https://$HOST:$PORT/product-composite/$PRODUCT_ID_OK -s"

# Verify access to Swagger/OpenAPI URLs
echo "Swagger/OpenAPI tests"
assertCurl 302 "curl -ks https://$HOST:$PORT/openapi/swagger-ui.html"
assertCurl 200 "curl -ksL https://$HOST:$PORT/openapi/swagger-ui.html"
assertCurl 200 "curl -ks https://$HOST:$PORT/openapi/webjars/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config"
assertCurl 200 "curl -ks https://$HOST:$PORT/openapi/v3/api-docs"
assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
assertEqual "https://$HOST:$PORT" "$(echo $RESPONSE | jq -r .servers[].url)"
assertCurl 200 "curl -ks https://$HOST:$PORT/openapi/v3/api-docs.yaml"


if [[ $@ == *"stop"* ]]
then
  echo "Tests completed, shutting down test environment..."
  echo "$ docker-compose down"
  docker-compose down
fi

echo "End, all tests OK: " `date`