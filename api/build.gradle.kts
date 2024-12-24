plugins {
    id("java")
    id("io.spring.dependency-management") version  "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.1"))
    implementation("org.springdoc:springdoc-openapi-starter-common:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel") {}