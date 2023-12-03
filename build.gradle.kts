plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

tasks {
    wrapper {
        gradleVersion = "8.4"
    }
}
