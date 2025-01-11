plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation(project(":util"))
    implementation(platform("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-config:${property("springCloudVersion")}")
    implementation("org.springframework.retry:spring-retry:${property("springRetryVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("io.micrometer:micrometer-tracing-bridge-otel:${property("micrometerVersion")}")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin:${property("oltpVersion")}")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("io.micrometer:micrometer-tracing-test")
    testImplementation("io.micrometer:micrometer-observation-test")
}

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}