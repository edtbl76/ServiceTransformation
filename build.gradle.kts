plugins {
    kotlin("jvm") version "2.1.0"
    id("java")
    id("io.spring.dependency-management") version  "1.1.7"
    id("org.springframework.boot") version "3.4.1"
    base
}

allprojects {
    group = "org.emangini.servolution"

    apply {
        plugin("java")
        plugin("idea")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.springframework.boot")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    // Global Dependencies
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        compileOnly("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.projectreactor:reactor-test")

    }
    tasks.test {
        useJUnitPlatform()
    }
}

// Four Microservices only - has to be outside allprojects directive
configure(listOf(
    project(":product-service"),
    project(":review-service"),
    project(":recommendation-service"),
    project(":product-composite-service"))) {

    dependencies {
        implementation(project(":api"))
        implementation(project(":util"))
        developmentOnly("org.springframework.boot:spring-boot-devtools")
    }
}


