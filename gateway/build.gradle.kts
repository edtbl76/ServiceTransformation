plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:${property("springCloudVersion")}")
}

tasks.register("prepareKotlinBuildScriptModel") {}