plugins {
    id("java")
    id("idea")
}

subprojects {

    group = "org.emangini.servolution"

    apply {
        plugin("java")
        plugin("idea")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }


    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("io.micrometer:micrometer-tracing-bom:${property("micrometerVersion")}"))
        implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
        compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
        compileOnly("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
        annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
        annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
        testImplementation(platform("org.junit:junit-bom:${property("junitVersion")}"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testAnnotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
        testAnnotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")

    }

    tasks.test {
        useJUnitPlatform()
        environment("DOCKER_HOST", "unix:///home/edmangini/.docker/desktop/docker.sock")
        environment("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", "/var/run/docker.sock")
    }

}