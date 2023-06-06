@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "info.marozzo.tournament"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(project(":tournament-core"))

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            }
        }
        val jvmTest by getting
    }
    jvmToolchain(11)
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "tournament-desktop"
            packageVersion = "1.0.0"
        }
    }
}

