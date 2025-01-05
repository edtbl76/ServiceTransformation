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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:${property("springCloudVersion")}")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${property("springDocVersion")}")
    testImplementation("org.projectlombok:lombok:${property("lombokVersion")}")
    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder:${property("springCloudVersion")}")
    testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}