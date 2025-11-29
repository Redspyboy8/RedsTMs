//import org.gradle.internal.extensions.stdlib.capitalized

plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
}

val neoforgeVersion: String by project
val kotlinForForgeVersion: String by project
val architecturyVersion: String by project
val minecraftVersion: String by project
val modId: String by project

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    neoForge {

    }
}

repositories {
//    maven(url = "${rootProject.projectDir}/deps")
    maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://maven.neoforged.net/releases")
    mavenLocal()
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentNeoForge: Configuration by configurations.getting

configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentNeoForge.extendsFrom(common)
}

dependencies {
    neoForge("net.neoforged:neoforge:$neoforgeVersion")
    modApi("dev.architectury:architectury-neoforge:$architecturyVersion")
    implementation("thedarkcolour:kotlinforforge-neoforge:$kotlinForForgeVersion") {
        exclude(group = "net.neoforged.fancymodloader", module = "loader")
    }

    common(project(":common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":common", "transformProductionNeoForge")) { isTransitive = false }
    implementation(kotlin("stdlib"))
//    // Cobblemon
//    val cobblemonVersion: String by project
//    val minecraftVersion: String by project
//    val cobblemonSnapshot: String by project
//    var cobblemonVersionFull = "$cobblemonVersion+$minecraftVersion"
//    if (cobblemonSnapshot.lowercase() == "true") {
//        cobblemonVersionFull += "-SNAPSHOT"
//    }
//    implementation("com.cobblemon:neoforge:$cobblemonVersionFull")

}
//sourceSets.main.get().resources.srcDir("src/generated/resources")







tasks.processResources {
    inputs.property("group", project.group)
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "group" to project.group,
            "version" to project.version,

            "mod_id" to modId,
            "minecraft_version" to minecraftVersion,
            "architectury_version" to architecturyVersion,
            "kotlin_for_forge_version" to kotlinForForgeVersion
        ))
    }
}

tasks.shadowJar {
    exclude("fabric.mod.json")
    exclude("architectury.common.json")
//    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    injectAccessWidener.set(true)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    dependsOn(tasks.shadowJar)
    archiveClassifier.set(null as String?)
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")
    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
}

components.getByName("java") {
    this as AdhocComponentWithVariants
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}