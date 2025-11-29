enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// This should match the folder name of the project, or else IDEA may complain (see https://youtrack.jetbrains.com/issue/IDEA-317606)
rootProject.name = "RedsTMS"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.impactdev.net/repository/development/")
        maven("https://cursemaven.com")
        maven("https://thedarkcolour.github.io/KotlinForForge/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://maven.parchmentmc.org")
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("jvm") version "2.0.21"
    }
}

//dependencyResolutionManagement {
//    versionCatalogs {
//        register("libs") {
//            from(files("libs.versions.toml"))
//        }
//    }
//}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("common")
include("fabric")
include("neoforge")