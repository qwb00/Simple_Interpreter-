plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.register<JavaExec>("runWithFile") {
    group = "application"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("MainKt")
    args = listOf(project.findProperty("file")?.toString())
}