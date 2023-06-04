pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	plugins {
		kotlin("multiplatform") version (extra["kotlin.version"] as String) apply false
		id("org.jetbrains.dokka") version (extra["kotlin.dokka.version"] as String) apply false
		id("org.jetbrains.compose") version (extra["compose.version"] as String) apply false
	}
}

rootProject.name = "HemaTournament"
include(":tournament-core", ":tournament-desktop")
