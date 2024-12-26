plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${property("springDocVersion")}")
    developmentOnly("org.springframework.boot:spring-boot-devtools:${property("springBootVersion")}")
}

tasks.register("prepareKotlinBuildScriptModel") {}