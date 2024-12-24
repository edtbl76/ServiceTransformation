plugins {
    id("org.springframework.boot") version "3.4.1"
}

dependencies {
    implementation(project(":api"))
    implementation(project(":util"))
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.register("prepareKotlinBuildScriptModel") {}