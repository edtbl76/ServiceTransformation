plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.register("prepareKotlinBuildScriptModel") {}