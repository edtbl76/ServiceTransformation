plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.emangini.servolution.discovery"
repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:${property("springCloudVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:${property("springCloudVersion")}")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("org.springframework.cloud:spring-cloud-starter-config:${property("springCloudVersion")}")
    implementation("org.springframework.retry:spring-retry:${property("springRetryVersion")}")
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