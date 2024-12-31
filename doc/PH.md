






### Testing Product Service Directly
```shell
http http://localhost:8080/product/3
```

### Testing Product Composite
```shell
http http://localhost:8080/product-composite/123 --unsorted
```




### Confirm you can retrieve a record (curl)

```shell
curl http://localhost:7000/product-composite/1 -s | jq
```



### Confirm you can receive a record (Httpie)

This is my preference because it shows the header and pretty-prints.

```shell
http http://localhost:7000/product-composite/1 --unsorted
```



### Confirm NotFound

1. Verify that a 404 (Not Found) is returned for a non existing productId
```shell
http http://localhost:7000/product-composite/13 --unsorted;
```




2. Verify that there aren't any recommendations for productId 113
```shell
http http://localhost:7000/product-composite/113 --unsorted;
```




3. Verify that there aren't any reviews for productId 213
```shell
http http://localhost:7000/product-composite/213 --unsorted;
```





### Verify that a 422 (Unprocessable Entity) for a productId that is out of range (-1)

```shell
http http://localhost:7000/product-composite/-1 --unsorted
````


### Verify that a 400 (Bad Request) for a productId that isn't a number (invalid format)

```shell
http http://localhost:7000/product-composite/invalidProductId --unsorted
```