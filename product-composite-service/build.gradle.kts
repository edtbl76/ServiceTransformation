plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.7.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.register("prepareKotlinBuildScriptModel") {}