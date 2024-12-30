plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools:${property("springBootVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:${property("springBootVersion")}")
    testImplementation("org.testcontainers:mongodb:${property("testcontainersVersion")}")
}

tasks.register("prepareKotlinBuildScriptModel") {}