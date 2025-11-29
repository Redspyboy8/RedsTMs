import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow")
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val architecturyVersion: String by project
val fabricKotlinVersion: String by project
val modId: String by project
val modName: String by project
val description: String by project
val modAuthor: String by project
val license: String by project
val credits: String by project
val javaVersion: String by project




architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentFabric: Configuration by configurations.getting

configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentFabric.extendsFrom(common)
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:$architecturyVersion")

    // Fabric Kotlin
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")


    common(project(":common", "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(":common", "transformProductionFabric")){
        isTransitive = false
    }

//    modImplementation("com.cobblemon:fabric:${project.properties["cobblemon_version"]}+$minecraft_version")
}
tasks.processResources {
//    val modLoader = project.name
//    val modAuthor: String by project
//    val isCommon = modLoader == rootProject.name

    inputs.property("group", project.group.toString())
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "group" to project.group.toString(),
            "version" to project.version,
            "mod_name" to modName,
            "mod_id" to modId,
            "mod_author" to modAuthor,
            "description" to description,
            "license" to license,
            "minecraft_version" to minecraftVersion,
            "architectury_version" to architecturyVersion,
            "fabric_kotlin_version" to fabricKotlinVersion,
            "fabric_loader_version" to fabricLoaderVersion,
            "java_version" to javaVersion,
            "flk_version" to fabricKotlinVersion,
        ))
    }
}

tasks.shadowJar {
    exclude("architectury.common.json")
    configurations = listOf(shadowCommon)
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

//val modId: String by project
//dependencies {
//    implementation(kotlin("stdlib"))
//
//    val parchmentVersion: String by project
//    val parchmentMCVersion: String by project
//
//    mappings(loom.layered {
//        officialMojangMappings()
//        parchment("org.parchmentmc.data:parchment-$parchmentMCVersion:$parchmentVersion@zip")
//    })
//
//    val minecraftVersion: String by project
//    val fabricLoaderVersion: String by project
//    val fabricApiVersion: String by project
//    val fabricKotlinVersion: String by project
//
//    minecraft("com.mojang:minecraft:$minecraftVersion")
//    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
//    modApi(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)
//    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
//
////    val forgeConfigApiVersion: String by project
////    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:$forgeConfigApiVersion")
//
////    // Cobblemon
////    val cobblemonVersion: String by project
////    val cobblemonSnapshot: String by project
////    var cobblemonVersionFull = "$cobblemonVersion+$minecraftVersion"
////    if (cobblemonSnapshot.lowercase() == "true") {
////        cobblemonVersionFull += "-SNAPSHOT"
////    }
////    modImplementation("com.cobblemon:fabric:$cobblemonVersionFull")
//}
//
//loom {
//    if (project(":common").file("src/main/resources/$modId.aw").exists()) {
//        accessWidenerPath.set(project(":common").file("src/main/resources/$modId.aw"))
//    }
//
//    @Suppress("UnstableApiUsage")
//    mixin {
//        defaultRefmapName.set("$modId.refmap.json")
//    }
//
//    mods {
//        create(modId) {
//            sourceSet(sourceSets.main.get())
//        }
//    }
//
//    runs {
//        named("client") {
//            client()
//            configName = "fabric - client"
//            ideConfigGenerated(true)
//            runDir("run")
//        }
//
//        named("server") {
//            server()
//            configName = "fabric - server"
//            ideConfigGenerated(true)
//            runDir("run")
//        }
//    }
//}
//
//tasks.withType<JavaCompile>().configureEach {
//    source(project(":common").sourceSets.getByName("main").allSource)
//}
//
//tasks.withType<ProcessResources>().configureEach {
//    from(project(":common").sourceSets.getByName("main").resources)
//    exclude("**/accesstransformer-nf.cfg")
//}
//
//tasks {
//    jar {
//        duplicatesStrategy = DuplicatesStrategy.INCLUDE
//        archiveClassifier.set("dev")
//    }
//
//    shadowJar {
//        archiveClassifier.set("dev-shadow")
//    }
//
//    remapJar {
//        inputFile.set(named<ShadowJar>("shadowJar").get().archiveFile)
//        dependsOn("shadowJar")
//    }
//}
//repositories {
//    mavenCentral()
//}
//kotlin {
//    jvmToolchain(21)
//}