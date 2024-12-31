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
        implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit:${property("springCloudVersion")}")
        implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:${property("springCloudVersion")}")

        compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
        compileOnly("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

        annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
        annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

        testCompileOnly("org.projectlombok:lombok:${property("lombokVersion")}")

        testImplementation(platform("org.junit:junit-bom:${property("junitVersion")}"))
        testImplementation("org.junit.jupiter:junit-jupiter:${property("junitVersion")}")
        testImplementation("org.springframework.boot:spring-boot-starter-test:${property("springBootVersion")}")
        testImplementation("io.projectreactor:reactor-test:${property("projectReactorVersion")}")
        testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder:${property("springCloudVersion")}")

        testAnnotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
        testAnnotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudBomVersion")}")
        }
    }

    tasks.test {
        useJUnitPlatform()
        environment("DOCKER_HOST", "unix://home/edmangini/.docker/desktop/docker.sock")
        environment("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", "/var/run/docker.sock")
        environment("TESTCONTAINERS_RYUK_DISABLED", "true")
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

