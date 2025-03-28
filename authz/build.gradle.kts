plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:${property("springCloudVersion")}")
    implementation("org.springframework.cloud:spring-cloud-starter-config:${property("springCloudVersion")}")
    implementation("org.springframework.retry:spring-retry:${property("springRetryVersion")}")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:${property("oauth2ServerVersion")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
