plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools:${property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${property("springBootVersion")}")
    implementation("mysql:mysql-connector-java:${property("mysqlVersion")}")
    testImplementation("org.testcontainers:mysql:${property("testcontainersVersion")}")
}

tasks.register("prepareKotlinBuildScriptModel") {}