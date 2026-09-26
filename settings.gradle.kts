pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
        maven("https://maven.minecraftforge.net") { name = "MinecraftForge" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        mavenCentral()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/central")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.5"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        val allVersions = listOf(
            "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
            "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5",
            "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11"
        )
        val active = "1.21.11"

        // Loader support per Minecraft version.
        // Fabric: every supported version. NeoForge: 1.21.x. Forge: 1.20.x (no 1.20.5 release).
        fun loadersFor(version: String): List<String> = buildList {
            add("fabric")
            if (version.startsWith("1.21")) add("neoforge")
            if (version.startsWith("1.20") && version != "1.20.5") add("forge")
        }

        fun createFor(version: String) {
            for (loader in loadersFor(version)) {
                version("$version-$loader", version).buildscript("build.$loader.gradle.kts")
            }
        }

        // CI passes `SC_VERSION` to create only the nodes for the version being built,
        // which avoids configuring every subproject in each job.
        val ciVersion = System.getenv("SC_VERSION")
        if (ciVersion.isNullOrBlank()) {
            allVersions.forEach(::createFor)
            vcsVersion = "$active-fabric"
        } else {
            createFor(ciVersion)
            vcsVersion = "$ciVersion-fabric"
        }
    }
}

rootProject.name = "eternal-firework-rocket"
