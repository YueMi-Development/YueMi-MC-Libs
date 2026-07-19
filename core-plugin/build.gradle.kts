plugins {
    java
    id("com.gradleup.shadow") version "8.3.0" // Shadow plugin
}

val pluginName: String by project
val repoUrl: String by project
val developerId: String by project
val developerName: String by project
val pluginVersion: String = project.version.toString()
val apiVersion: String by project
val authors: String by project
val contributors: String by project

dependencies {
    implementation("org.bstats:bstats-bukkit:3.2.1")
    implementation(project(":core-api"))
    implementation("org.yuemi:mc-config-libs:1.0.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("net.momirealms:craft-engine-bukkit:26.6.2")
    compileOnly("net.momirealms:craft-engine-core:26.6.2")
    compileOnly("com.nexomc:nexo:1.21.0")
    compileOnly("beer.devs:itemsadder-api:4.0.17-beta-12-test-8")
}

tasks.processResources {
    val bstatsPluginId = project.findProperty("bstatsPluginId") as? String ?: ""
    val props = mapOf(
        "pluginName" to pluginName,
        "version" to pluginVersion,
        "apiVersion" to apiVersion,
        "authors" to authors,
        "contributors" to contributors,
        "bstatsPluginId" to bstatsPluginId
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
    archiveClassifier.set("raw")

    manifest {
        attributes(
            "Implementation-Title" to pluginName,
            "Implementation-Version" to pluginVersion,
            "Implementation-Vendor" to developerName,
            "License" to "LGPL-3.0-or-later"
        )
    }
}

val deleteRawJar = tasks.register<Delete>("deleteRawJar") {
    delete(tasks.jar.flatMap { it.archiveFile })
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set(pluginName)
    archiveVersion.set(pluginVersion)
    archiveClassifier.set("")

    configurations = listOf(project.configurations.runtimeClasspath.get())

    relocate("org.bstats", "${project.group}.libs.plugin.bstats")
    relocate("org.yuemi.config", "${project.group}.libs.config")

    finalizedBy(deleteRawJar)
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

