plugins {
    kotlin("jvm") version "1.4.20"
}

group = "com.hcelaloner"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin STD
    implementation(kotlin("stdlib-jdk8"))

    // Kotlin Coroutines
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.4.3"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

    // Junit 5
    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // AssertJ
    testImplementation("org.assertj:assertj-core:3.19.0")

    // MockK
    testImplementation("io.mockk:mockk:1.11.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}