dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${property("springBootVersion")}"))
    implementation("org.springdoc:springdoc-openapi-starter-common:${property("springDocVersion")}")
}

tasks.register("prepareKotlinBuildScriptModel") {}