pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	plugins {
		id("me.qoomon.git-versioning") version "6.4.2"
		kotlin("multiplatform") version (extra["kotlin.version"] as String) apply false
		id("org.jetbrains.dokka") version (extra["kotlin.dokka.version"] as String) apply false
	}
}

rootProject.name = "HemaTournament"
include(":tournament-core")
