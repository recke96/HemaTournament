import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
}

group = "info.marozzo.tournament"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

ksp {
    arg("lyricist.internalVisibility", "true")
    arg("lyricist.packageName", "info.marozzo.tournament.desktop.i18n")
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

                implementation("cafe.adriel.lyricist:lyricist:1.4.2")
                kotlin.srcDir("$buildDir/generated/ksp/jvm/jvmMain/kotlin")

                implementation("io.arrow-kt:arrow-core:1.2.0")
                implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")
            }
        }
        val jvmTest by getting
    }
}

dependencies {
    add("kspJvm", "cafe.adriel.lyricist:lyricist-processor:1.4.2")
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

