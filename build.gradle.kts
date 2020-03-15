plugins {
    kotlin("jvm") version "1.3.70"
    id("org.jetbrains.dokka") version "0.10.1"
}

group = "com.hcelaloner"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    // Kotlin STD
    implementation(kotlin("stdlib-jdk8"))

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")

    // Kotlin Coroutines Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.4")

    // Junit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:+")
    testImplementation("org.junit.jupiter:junit-jupiter-params:+")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:+")

    // AssertJ
    testImplementation("org.assertj:assertj-core:+")

    // MockK
    testImplementation("io.mockk:mockk:+")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }
    test {
        useJUnitPlatform()
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks.dokka)
}