import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
    jacoco

    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.20"
}

group = "ru.university.toursystem"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3",
    )

    implementation(
        "org.jetbrains.kotlin:kotlin-reflect:1.9.22",
    )

    testImplementation(kotlin("test"))

    testImplementation(
        "org.junit.jupiter:junit-jupiter-api:5.10.2",
    )

    testRuntimeOnly(
        "org.junit.jupiter:junit-jupiter-engine:5.10.2",
    )
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("TourSystemKt")
}

tasks.withType<Test> {

    useJUnitPlatform()

    jvmArgs(
        "-Dfile.encoding=UTF-8",
    )

    testLogging {

        events(
            TestLogEvent.PASSED,
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR,
        )

        showStandardStreams = true

        exceptionFormat = TestExceptionFormat.FULL

        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    finalizedBy(
        tasks.jacocoTestReport,
    )
}

tasks.withType<JavaCompile> {

    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {

    kotlinOptions {

        jvmTarget = "21"

        freeCompilerArgs +=
            listOf(
                "-Xjsr305=strict",
            )
    }
}

tasks.withType<JavaExec> {

    jvmArgs(
        "-Dfile.encoding=UTF-8",
    )
}

jacoco {

    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {

    dependsOn(
        tasks.test,
    )

    reports {

        xml.required.set(true)

        html.required.set(true)

        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {

    violationRules {

        rule {

            limit {

                minimum = "1.0".toBigDecimal()
            }
        }
    }
}

ktlint {

    filter {

        exclude("**/generated/**")
    }

    reporters {

        reporter(
            org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN,
        )

        reporter(
            org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE,
        )
    }
}

tasks.coverallsJacoco {

    dependsOn(
        tasks.test,
        tasks.jacocoTestReport,
    )
}
