// Workaround for:
// https://youtrack.jetbrains.com/issue/KT-49811/KJS-Gradle-False-positive-The-Kotlin-Gradle-plugin-was-loaded-multiple-times-in-different-subprojects-caused-by-android-and-js
plugins {
    alias(libs.plugins.kotlin.mpp) apply false
    alias(libs.plugins.kotlinx.kover) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins. kotest.mpp) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dokka) apply false
}

