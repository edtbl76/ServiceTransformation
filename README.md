# ServiceTransformation

## Version Information

### Gradle

```text
./gradlew -v

------------------------------------------------------------
Gradle 8.10
------------------------------------------------------------

Build time:    2024-08-14 11:07:45 UTC
Revision:      fef2edbed8af1022cefaf44d4c0514c5f89d7b78

Kotlin:        1.9.24
Groovy:        3.0.22
Ant:           Apache Ant(TM) version 1.10.14 compiled on August 16 2023
Launcher JVM:  17.0.7 (Amazon.com Inc. 17.0.7+7-LTS)
Daemon JVM:    /home/edmangini/.sdkman/candidates/java/17.0.7-amzn 
(no JDK specified, using current Java home)
OS:            Linux 6.9.3-76060903-generic amd64
```

## Java

```text
java -version
openjdk version "17.0.7" 2023-04-18 LTS
OpenJDK Runtime Environment Corretto-17.0.7.7.1 (build 17.0.7+7-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.7.7.1 
(build 17.0.7+7-LTS, mixed mode, sharing)
```

## Spring Framework
```text
SpringBoot: 3.4.1
```

---

## Build the project

1. Make sure you are in the ./ServiceTransformation directory (the home of the project)
2. build the project
```text
./gradlew build
```

**Expected Output**
```text

BUILD SUCCESSFUL in 22s
33 actionable tasks: 10 executed, 23 up-to-date
```

**NOTE**
This will only work w/ Gradle 8. 
```text
Deprecated Gradle features were used in this build, 
making it incompatible with Gradle 9.0.
```
---

---

## Launch Microservices

### Intellij 

1. Right click on the service you want to launch
2. Navigate to **_src/main/java/org/emangini/servolution/core/\<service_name\>_**
3. Right click on **_\<ServiceName\>ServiceApplication_**
4. Select **_Run \<ServiceName\>...main()_** 

Output will show in the run terminal

### CLI

You can run these in the background or open up multiple terminal windows

1. Get to the home directory: **_ServiceTransformation_**
2. Run each of these commands from the home directory in a different window:

```text
java -jar product-compose-service/build/libs/*.jar &
java -jar product-service/build/libs/*.jar &
java -jar recommendation-service/build/libs/*.jar &
java -jar review-service/build/libs/*.jar &
```
NOTE: That even though these are running in the background, the output should display in the Terminal. 

Example: 
```text
2024-12-22T21:48:33.824-05:00  INFO 735987 --- [           main] s.c.p.ProductCompositeServiceApplication 
: Started ProductCompositeServiceApplication in 1.592 seconds
```
The number after the word **INFO** is the terminal process. 

```shell
(base) edmangini@pop-os:~/IdeaProjects/ServiceTransformation$ ps aux | grep 735987
edmangi+  735987  2.5  0.4 21594296 320252 pts/4 SNl  21:48   0:11 java -jar product-composite-service/build/libs/product-composite-service.jar product-composite-service/build/libs/product-composite-service-plain.jar
```

#### Killing Them Gracefully. 

Bring the service to the foreground and terminate it w/ CTRL-C
```shell
fg
java -jar product-service/build/libs/*.jar
^C2024-12-22T21:56:55.800-05:00  INFO 739147 --- [ionShutdownHook] o.s.b.w.embedded.netty.GracefulShutdown  : Commencing graceful shutdown. Waiting for active requests to complete
2024-12-22T21:56:55.805-05:00  INFO 739147 --- [ netty-shutdo
```

---

