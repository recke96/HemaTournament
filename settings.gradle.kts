pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	plugins {
		kotlin("multiplatform") version "1.9.0" apply false
		id("org.jetbrains.dokka") version "1.8.20" apply false
		id("org.jetbrains.compose") version "1.4.3" apply false
		id("io.kotest.multiplatform") version "5.6.2" apply false
		id("org.jetbrains.kotlinx.kover") version "0.7.3" apply false
	}
}

rootProject.name = "HemaTournament"
include(":tournament-core", ":tournament-desktop")
