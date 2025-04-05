# Building the Software

## Table of Contents

### Gradle
- [Build Project (Gradle)](#build-project-gradle)
- [Build Individual Service (gradle)](#build-indiviual-service-gradle)

### Docker
- [Building Docker Images (w/ docker-compose)](#building-docker-images-w-docker-compose)


## Documentation
- [Readme](../README.md)
- [Building](BUILD.md)
- [Release Notes](RELEASE.md)
- [Running Services](RUNNING.md)
- [Testing Services](TESTING.md)
- [Support](SUPPORT.md)
---

## Build Project (Gradle)

1. Make sure you are in the ./ServiceTransformation directory (the home of the project)
2. build the project
```text
./gradlew build
```

Status of output 
```text
BUILD SUCCESSFUL in 8s
58 actionable tasks: 58 up-to-date
```


---

## Build Indiviual Service (Gradle)

```shell
./gradlew build :product-service:build
```

## Building Docker Images w/ Docker Compose
```shell
docker-compose build
```


### Validate
```shell
docker images | grep "servicetransformation"
```
```text
servicetransformation/product-composite-service   latest                                     9b7ca1ffd610   6 minutes ago   577MB
servicetransformation/recommendation-service      latest                                     31ce623ff8b6   7 minutes ago   573MB
servicetransformation/product-service             latest                                     1afe450d003f   7 minutes ago   573MB
servicetransformation/review-service              latest                                     5cfa0572d219   7 minutes ago   570MB
servicetransformation/gateway                     latest                                     d77b57a69d9b   2 hours ago     514MB
servicetransformation/config-server               latest                                     da450073bb54   2 hours ago     511MB
servicetransformation/auth-server                 latest                                     7fce5662e91e   25 hours ago    504MB
```

---

