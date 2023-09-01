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
                implementation(compose.material3)

                implementation(project(":tournament-core"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

                implementation("com.arkivanov.mvikotlin:mvikotlin:3.2.1")
                implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.2.1")
                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:3.2.1")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "info.marozzo.tournament.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "tournament-desktop"
            packageVersion = "1.0.0"
            licenseFile = project.rootProject.file("LICENCE")
        }
    }
}

