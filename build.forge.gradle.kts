import org.gradle.api.tasks.SourceSetContainer

plugins {
    id("net.minecraftforge.gradle") version "6.0.54"
}

val mcVersion = sc.current.version
val modId = property("mod.id") as String
val modVersion = property("mod.version") as String
val mavenGroup = property("mod.group") as String
val forgeVersion = property("deps.forge_loader") as String

val javaVersion = if (sc.current.parsed >= "1.20.5") JavaVersion.VERSION_21 else JavaVersion.VERSION_17

group = mavenGroup
version = "$modVersion+$mcVersion"
base {
    archivesName = "$modId-$mcVersion-forge"
}

repositories {
    maven("https://maven.minecraftforge.net")
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

minecraft {
    mappings("official", mcVersion)
}

val sourceSets = extensions.getByType<SourceSetContainer>()
sourceSets.getByName("main").java.srcDir(rootProject.file("src/forge/java"))

dependencies {
    "minecraft"("net.minecraftforge:forge:$mcVersion-$forgeVersion")
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
    inputs.property("forge_dep", forgeVersion)
    filesMatching("META-INF/mods.toml") {
        expand(
            mapOf(
                "version" to modVersion,
                "mc_dep" to mcVersion,
                "forge_dep" to forgeVersion
            )
        )
    }
    exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
}
