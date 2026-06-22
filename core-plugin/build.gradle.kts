plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1" // Shadow plugin
}

val pluginName: String by project
val repoUrl: String by project
val developerId: String by project
val developerName: String by project
val pluginVersion: String = project.version.toString()
val apiVersion: String by project
val authors: String by project

dependencies {
    implementation(project(":core-api"))
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("net.momirealms:craft-engine-bukkit:26.6.2")
    compileOnly("net.momirealms:craft-engine-core:26.6.2")
}

tasks.processResources {
    val props = mapOf(
        "pluginName" to pluginName,
        "version" to pluginVersion,
        "apiVersion" to apiVersion,
        "authors" to authors
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    archiveBaseName.set(pluginName)
    archiveVersion.set(pluginVersion)
    archiveClassifier.set("")

    manifest {
        attributes(
            "Implementation-Title" to pluginName,
            "Implementation-Version" to pluginVersion,
            "Implementation-Vendor" to developerName,
            "License" to "LGPL-3.0-or-later"
        )
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set(pluginName)
    archiveVersion.set(pluginVersion)
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

