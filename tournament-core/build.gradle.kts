@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.dokka")
}

group = "info.marozzo.tournament"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	dokkaPlugin("org.jetbrains.dokka:mathjax-plugin:1.8.10")
}

kotlin {
	jvm {
		jvmToolchain(17)
		withJava()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
				implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
				implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
				implementation("ch.tutteli.atrium:atrium-fluent:1.0.0")
			}
		}

		val jvmMain by getting
		val jvmTest by getting {
			dependencies {
				implementation(kotlin("test-junit"))
			}
		}
	}
}

