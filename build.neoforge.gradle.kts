import org.gradle.api.tasks.SourceSetContainer

plugins {
    id("net.neoforged.moddev") version "2.0.147"
}

val mcVersion = sc.current.version
val modId = property("mod.id") as String
val modVersion = property("mod.version") as String
val mavenGroup = property("mod.group") as String
val neoForgeVersion = property("deps.neo_loader") as String

val javaVersion = JavaVersion.VERSION_21

group = mavenGroup
version = "$modVersion+$mcVersion"
base {
    archivesName = "$modId-$mcVersion-neoforge"
}

repositories {
    maven("https://maven.neoforged.net/releases")
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

val sourceSets = extensions.getByType<SourceSetContainer>()
sourceSets.getByName("main").java.srcDir(rootProject.file("src/neoforge/java"))

neoForge {
    version = neoForgeVersion

    mods {
        register(modId) {
            sourceSet(sourceSets.getByName("main"))
        }
    }
}

java {
    withSourcesJar()
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion.majorVersion.toInt())
}

tasks.processResources {
    inputs.property("version", modVersion)
    inputs.property("mc_dep", mcVersion)
    inputs.property("neo_dep", neoForgeVersion)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(
            mapOf(
                "version" to modVersion,
                "mc_dep" to mcVersion,
                "neo_dep" to neoForgeVersion
            )
        )
    }
    exclude("fabric.mod.json", "META-INF/mods.toml")
}
