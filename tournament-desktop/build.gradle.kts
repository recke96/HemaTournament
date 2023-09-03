import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.mpp)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
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

                implementation(libs.kotlinx.coroutines.core.jvm)
                implementation(libs.kotlinx.coroutines.swing)

                implementation(libs.kotlinx.collections.immutable)

                implementation(libs.bundles.mvi)

                implementation(libs.lyricist)
                kotlin.srcDir("${layout.buildDirectory}/generated/ksp/jvm/jvmMain/kotlin")

                implementation(libs.bundles.arrow)

                implementation(libs.bundles.koin)

                implementation(libs.appdirs)

                implementation(libs.sqldelight.driver)
                runtimeOnly(libs.h2)
            }
        }
        val jvmTest by getting
    }
}

dependencies {
    add("kspJvm", libs.lyricist.processor)
}

sqldelight {
    databases {
        create("AppDb") {
            packageName.set("info.marozzo.tournament.desktop.db")
            dialect(libs.sqldelight.dialect.h2)
            deriveSchemaFromMigrations.set(true)
            srcDirs("src/jvmMain/sqldelight")
        }
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

