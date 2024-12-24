dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.1"))
    implementation("org.springdoc:springdoc-openapi-starter-common:2.7.0")
}

tasks.register("prepareKotlinBuildScriptModel") {}