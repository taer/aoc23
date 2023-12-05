import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.cli.jvm.main

plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies{
    val kotestVersion = "5.8.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}
tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "day05.Day05Kt"))
        }
    }
}
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks {
    wrapper {
        gradleVersion = "8.4"
    }
}
