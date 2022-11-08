import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}


group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

allOpen{
    annotation("com.example.apiarchetypereactive.model.MOpen")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:mockwebserver")
    implementation("com.github.jasync-sql:jasync-r2dbc-mysql:2.1.7")
    testImplementation("io.mockk:mockk:1.13.2")
//    implementation("org.springframework.data:spring-data-r2dbc:1.5.5")
//    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")
    runtimeOnly("com.mysql:mysql-connector-j:8.0.31")
//    testRuntimeOnly("com.mysql:mysql-connector-j:8.0.31")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

