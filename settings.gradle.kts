pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	plugins {
		kotlin("multiplatform") version "1.8.20" apply false
		id("org.jetbrains.dokka") version "1.8.20" apply false
		id("org.jetbrains.compose") version "1.4.0" apply false
	}
}

rootProject.name = "HemaTournament"
include(":tournament-core", ":tournament-desktop")
