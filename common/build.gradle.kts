val enabledPlatforms: String by project
val modId: String by project
val minecraftVersion: String by project
val architecturyVersion: String by project
val fabricLoaderVersion: String by project
val neoformVersion: String by project



architectury {
    common(enabledPlatforms.toString().split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/$modId.accesswidener"))
}

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modApi("dev.architectury:architectury:$architecturyVersion")

    implementation(kotlin("stdlib-jdk8"))
//    modCompileOnly("com.cobblemon:mod:${rootProject.property("cobblemon_version")}+${minecraft_version}") {
//        isTransitive = false
//    }
//
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}