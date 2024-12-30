plugins {
    kotlin("jvm") version "2.1.0"
    id("java")
    id("io.spring.dependency-management") version  "1.1.7"
    base
}

allprojects {
    group = property("group") as String
    version = property("projectVersion") as String

    apply {
        plugin("java")
        plugin("idea")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator:${property("springBootVersion")}")
        implementation("org.springframework.boot:spring-boot-starter-webflux:${property("springBootVersion")}")
        implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")

        compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
        compileOnly("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

        annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
        annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

        testImplementation(platform("org.junit:junit-bom:${property("junitVersion")}"))
        testImplementation("org.junit.jupiter:junit-jupiter:${property("junitVersion")}")
        testImplementation("org.springframework.boot:spring-boot-starter-test:${property("springBootVersion")}")
        testImplementation("io.projectreactor:reactor-test:${property("projectReactorVersion")}")

        testAnnotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
    }

    tasks.test {
        useJUnitPlatform()
    }

}

// Microservices-only
configure(listOf(
    project(":product-service"),
    project(":review-service"),
    project(":recommendation-service"),
    project(":product-composite-service")
)) {


    dependencies {
        implementation(project(":api"))
        implementation(project(":util"))
    }
}

// Non-Composite Microservices-only
configure(listOf(
    project(":product-service"),
    project(":review-service"),
    project(":recommendation-service")
)) {


    dependencies {
        implementation("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
        testImplementation("org.testcontainers:testcontainers:${property("testcontainersVersion")}")
        testImplementation("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    }
}

