plugins {
    id("org.springframework.boot") version "3.4.1"
}


dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:${property("springCloudVersion")}")
    implementation("org.glassfish.jaxb:jaxb-runtime:${property("jaxbRuntimeVersion")}")

}


tasks.register("prepareKotlinBuildScriptModel") {}
