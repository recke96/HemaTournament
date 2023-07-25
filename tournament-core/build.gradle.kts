plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("io.kotest.multiplatform")
}

group = "info.marozzo.tournament"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    dokkaPlugin("org.jetbrains.dokka:mathjax-plugin:1.8.20")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:5.6.2")
                implementation("io.kotest:kotest-property:5.6.2")
                implementation("io.kotest.extensions:kotest-property-arbs:2.1.2")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

            }
        }

        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
            }
        }
    }
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
    testLogging.setShowStandardStreams(true)
}

