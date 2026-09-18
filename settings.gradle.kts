pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        mavenCentral()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/central")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.3"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"
    create(rootProject) {
        val allVersions = listOf(
            "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
            "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5",
            "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11"
        )
        val ciVersion = System.getenv("SC_VERSION")
        val active = "1.21.11"
        val versions = when {
            ciVersion.isNullOrBlank() -> allVersions
            ciVersion == active -> listOf(active)
            else -> listOf(active, ciVersion)
        }
        versions.forEach { version(it, it) }
        vcsVersion = active
    }
}

rootProject.name = "eternal-firework-rocket"
