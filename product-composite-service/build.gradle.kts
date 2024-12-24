plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    implementation(project(":api"))
    implementation(project(":util"))
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.7.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.register("prepareKotlinBuildScriptModel") {}