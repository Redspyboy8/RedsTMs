import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.utils.extendsFrom
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import java.text.SimpleDateFormat
import java.util.*

plugins {
    java
    idea
    kotlin("jvm") version "2.0.21"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.0" apply false
    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
}

val minecraftVersion: String by project

architectury {
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
    loom.silentMojangMappingsLicense()

    repositories {
        maven(url = "https://maven.parchmentmc.org")
        maven(url = "https://maven.neoforged.net/releases/")
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
        maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
        maven(url = "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        maven(url = "https://maven.impactdev.net/repository/development/")
        maven(url = "https://api.modrinth.com/maven")
        maven(url = "https://maven.jt-dev.tech/releases")
        maven(url = "https://maven.jt-dev.tech/snapshots")
    }

    dependencies {
        val parchmentMCVersion: String by project
        val parchmentVersion: String by project
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"(loom.layered{
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$parchmentMCVersion:$parchmentVersion@zip")
        })
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    val archiveBaseName: String by project
    val mavenGroup: String by project

    base.archivesName.set(archiveBaseName)
    version = project.version
    group = mavenGroup;

    repositories {
        mavenCentral()
        // Add repositories to retrieve artifacts from in here.
        // You should only use this when depending on other mods because
        // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
        // See https://docs.gradle.org/current/userguide/declaring_repositories.html
        // for more information about repositories.
        maven("https://maven.impactdev.net/repository/development/")
    }

    dependencies {
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    kotlin {
       jvmToolchain(21)
    }

    java {
        withSourcesJar()
    }
}

//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
//import org.jetbrains.kotlin.gradle.utils.extendsFrom
//import net.fabricmc.loom.api.LoomGradleExtensionAPI
//import java.text.SimpleDateFormat
//import java.util.*
//
//plugins {
//    java
//    idea
//    kotlin("jvm") version "2.0.21"
//    id("architectury-plugin") version "3.4-SNAPSHOT"
//    id("com.gradleup.shadow") version "8.3.0" apply false
//    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
//    id("net.neoforged.moddev") version "1.0.19" apply false
//
//
//}
//
//val minecraftVersion: String by project
//
//
//architectury {
//    minecraft = rootProject.property("minecraftVersion").toString()
//}
//
//repositories {
//    mavenCentral()
//}
//
//
//subprojects {
//    apply(plugin = "java")
//    apply(plugin = "kotlin")
//    apply(plugin = "dev.architectury.loom")
//    apply(plugin = "architectury-plugin")
//    apply(plugin = "maven-publish")
//
//    val modLoader = project.name
//    val modId: String by project
//    val modName: String by project
//    val modAuthor: String by project
//    val isCommon = modLoader == rootProject.name
//
//    base {
//        archivesName.set("$modId-$modLoader-$minecraftVersion")
//    }
//
//    extensions.configure<JavaPluginExtension> {
//        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
//        withSourcesJar()
//    }
//
//    repositories {
//        mavenCentral()
//        maven(url = "https://maven.parchmentmc.org")
//        maven(url = "https://maven.neoforged.net/releases/")
//        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
//        maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
//        maven(url = "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
//        maven(url = "https://maven.impactdev.net/repository/development/")
//        maven(url = "https://api.modrinth.com/maven")
//        maven(url = "https://maven.jt-dev.tech/releases")
//        maven(url = "https://maven.jt-dev.tech/snapshots")
//    }
//
//    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
//
//    dependencies {
//        val parchmentMCVersion: String by project
//        val parchmentVersion: String by project
//        "minecraft"("com.mojang:minecraft:$minecraftVersion")
//        "mappings"(loom.layered{
//            officialMojangMappings()
//            parchment("org.parchmentmc.data:parchment-$parchmentMCVersion:$parchmentVersion@zip")
//        })
//    }
//
//    val commonDep by configurations.creating
//    configurations.implementation.extendsFrom(configurations.named(commonDep.name))
//
//    java {
//        withSourcesJar()
//        modularity.inferModulePath = true
//    }
//
//    tasks.test {
//        // There are no tests and the NeoForge build fails
//        exclude("**/*")
//    }
//
//    tasks.jar {
//        from(rootProject.file("LICENSE")) {
//            rename { "${it}_$modId" }
//        }
//
//        manifest {
//            attributes(
//                "Specification-Title" to modId,
//                "Specification-Vendor" to modAuthor,
//                "Specification-Version" to project.version,
//                "Implementation-Title" to project.name,
//                "Implementation-Version" to project.version,
//                "Implementation-Vendor" to modAuthor,
//                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
//                "Timestamp" to System.currentTimeMillis(),
//                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
//                "Built-On-Minecraft" to project.property("minecraftVersion")
//            )
//        }
//    }
//
//    tasks.processResources {
//        val version: String by project
//        val group: String by project
//        val minecraftVersionRange: String by project
//        val fabricApiVersion: String by project
//        val fabricLoaderVersion: String by project
//        val fabricKotlinVersion: String by project
//        val neoforgeVersion: String by project
//        val neoforgeVersionRange: String by project
//        val kotlinForgeLoaderRange: String by project
//        val forgeConfigApiVersion: String by project
////        val cobblemonVersion: String by project
////        val cobblemonVersionRange: String by project
//        val license: String by project
//        val description: String by project
//
//        val expandProps = mapOf(
//            "version" to version,
//            "group" to group, //Else we target the task's group.
//            "minecraft_version" to minecraftVersion,
//            "minecraft_version_range" to minecraftVersionRange,
//            "fabric_api_version" to fabricApiVersion,
//            "fabric_loader_version" to fabricLoaderVersion,
//            "fabric_kotlin_version" to fabricKotlinVersion,
//            "neoforge_version" to neoforgeVersion,
//            "neoforge_loader_version_range" to neoforgeVersionRange,
//            "kotlin_forge_loader_range" to kotlinForgeLoaderRange,
//            "forge_config_api_version" to forgeConfigApiVersion,
////        "cobblemon_version" to cobblemonVersion,
////        "cobblemon_version_range" to cobblemonVersionRange,
//            "mod_name" to modName,
//            "mod_author" to modAuthor,
//            "mod_id" to modId,
//            "license" to license,
//            "description" to description
//        )
//
//        filesMatching(listOf("pack.mcmeta", "*.mixins.json", "META-INF/*.mods.toml", "fabric.mod.json")) {
//            expand(expandProps)
//        }
//        inputs.properties(expandProps)
//
//
//    }
//
//
//    if(!isCommon) {
//        if (!isCommon) {
//            apply(plugin = "com.gradleup.shadow")
//
//            dependencies {
//                // This is runtimeLib, because NG doesn't add the common classes to the runtime classpath correctly
//                commonDep(project(":common")) {
//                    isTransitive = false
//                }
//            }
//
//            // Include common classes in jar and shadowJar output
//            listOf(tasks.jar, tasks.named<ShadowJar>("shadowJar")).forEach {
//                it { from(project(":common").sourceSets.main.get().output) }
//            }
//
//            tasks.named<ShadowJar>("shadowJar") {
//                configurations = emptyList()
//            }
//        }
//
//        tasks.withType<JavaCompile> {
//            options.encoding = "UTF-8"
//            targetCompatibility = JavaVersion.VERSION_21.majorVersion
//            sourceCompatibility = JavaVersion.VERSION_21.majorVersion
//        }
//
//    }
//}
//
//kotlin {
//    jvmToolchain(21)
//}
//
//// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
//idea {
//    module {
//        isDownloadSources = true
//        isDownloadJavadoc = true
//    }
//}

