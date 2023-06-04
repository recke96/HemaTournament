// Workaround for:
// https://youtrack.jetbrains.com/issue/KT-49811/KJS-Gradle-False-positive-The-Kotlin-Gradle-plugin-was-loaded-multiple-times-in-different-subprojects-caused-by-android-and-js
plugins {
    kotlin("multiplatform") apply false
}

