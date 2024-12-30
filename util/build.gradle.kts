dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.1"))
    implementation(project(":api"))

}


tasks.register("prepareKotlinBuildScriptModel") {}