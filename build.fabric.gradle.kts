import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.tasks.SourceSetContainer

plugins {
    alias(libs.plugins.loom)
    `maven-publish`
}

val mcVersion = sc.current.version
val modId = property("mod.id") as String
val modVersion = property("mod.version") as String
val mavenGroup = property("mod.group") as String
val yarnMappings = property("deps.yarn_mappings") as String
val fabricApiVersion = property("deps.fabric_api_version") as String
val fabricLoaderVersion = property("deps.fabric_loader") as String

val javaVersion = if (sc.current.parsed >= "1.20.5") JavaVersion.VERSION_21 else JavaVersion.VERSION_17

group = mavenGroup
version = "$modVersion+$mcVersion"
base {
    archivesName = "$modId-$mcVersion"
}

repositories {
    maven("https://maven.fabricmc.net")
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://repository.hanbings.io/proxy") { name = "Hanbings Fabric Mirror" }
    maven("https://maven.aliyun.com/repository/central")
}

val sourceSets = extensions.getByType<SourceSetContainer>()

extensions.configure<LoomGradleExtensionAPI> {
    splitEnvironmentSourceSets()

    mods.register(modId) {
        sourceSet(sourceSets.getByName("main"))
        sourceSet(sourceSets.getByName("client"))
    }
}

dependencies {
    add("minecraft", "com.mojang:minecraft:$mcVersion")
    add("mappings", "net.fabricmc:yarn:$yarnMappings:v2")
    add("modImplementation", "net.fabricmc:fabric-loader:$fabricLoaderVersion")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
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
    inputs.property("java_dep", javaVersion.majorVersion)
    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to modVersion,
                "mc_dep" to mcVersion,
                "java_dep" to javaVersion.majorVersion
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = modId
            from(components["java"])
        }
    }
}
