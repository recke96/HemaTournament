plugins {
    alias(libs.plugins.kotlin.mpp)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotest.mpp)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.ksp)
}

group = "info.marozzo.tournament"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    dokkaPlugin(libs.dokka.mathjax)
}

kotlin {
    jvm {
        jvmToolchain(21)
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)

                implementation(libs.bundles.arrow)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.kotest.common)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.jvm)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging.setShowStandardStreams(true)
    val buildDir = layout.buildDirectory.asFile.get().path
    systemProperty("gradle.build.dir", buildDir)
}
