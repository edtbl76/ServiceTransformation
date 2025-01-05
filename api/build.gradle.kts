plugins {
    java
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {


    implementation(platform("org.springframework.boot:spring-boot-dependencies:${property("springBootVersion")}"))
    implementation("org.springdoc:springdoc-openapi-starter-common:${property("springDocVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<Test> {
    useJUnitPlatform()
}