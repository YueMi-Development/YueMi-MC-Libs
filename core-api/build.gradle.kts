plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val pluginName: String by project
val yuemiMavenSnapshot: String by project
val yuemiMavenRelease: String by project
val repoUrl: String by project
val developerId: String by project
val developerName: String by project

require(pluginName.isNotBlank())
require(yuemiMavenSnapshot.isNotBlank())
require(yuemiMavenRelease.isNotBlank())
require(repoUrl.isNotBlank())
require(developerId.isNotBlank())
require(developerName.isNotBlank())

dependencies {
    api(project(":module-economy"))
    api(project(":module-items"))
    api(project(":module-gui"))
    api(project(":module-event"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    archiveBaseName.set(pluginName)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("raw") // We set this to raw so the shaded jar can be the main artifact
}

tasks.named<Jar>("sourcesJar") {
    archiveBaseName.set(pluginName)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("sources")
    
    // Include sources of the sub-modules
    from(project(":module-economy").sourceSets.main.get().allSource)
    from(project(":module-items").sourceSets.main.get().allSource)
    from(project(":module-gui").sourceSets.main.get().allSource)
    from(project(":module-event").sourceSets.main.get().allSource)
}

tasks.named<Javadoc>("javadoc") {
    // Include sub-modules sources for javadoc generation
    source(project(":module-economy").sourceSets.main.get().allSource)
    source(project(":module-items").sourceSets.main.get().allSource)
    source(project(":module-gui").sourceSets.main.get().allSource)
    source(project(":module-event").sourceSets.main.get().allSource)
    
    classpath += files(
        project(":module-economy").sourceSets.main.get().compileClasspath,
        project(":module-items").sourceSets.main.get().compileClasspath,
        project(":module-gui").sourceSets.main.get().compileClasspath,
        project(":module-event").sourceSets.main.get().compileClasspath
    )

    title = "$pluginName ${project.version} API"
    (options as StandardJavadocDocletOptions).apply {
        windowTitle = "$pluginName ${project.version} API"
        docTitle = "$pluginName ${project.version} API"
    }
}

tasks.named<Jar>("javadocJar") {
    archiveBaseName.set(pluginName)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("javadoc")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set(pluginName)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("") // Main artifact classifier is empty

    // Merge compiled classes and resources of the sub-modules
    from(project(":module-economy").sourceSets.main.get().output)
    from(project(":module-items").sourceSets.main.get().output)
    from(project(":module-gui").sourceSets.main.get().output)
    from(project(":module-event").sourceSets.main.get().output)
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // Publish shadowJar as the main api artifact, alongside sources and javadoc jars
            artifact(tasks.named("shadowJar"))
            artifact(tasks.named("sourcesJar"))
            artifact(tasks.named("javadocJar"))

            artifactId = "$pluginName-api"

            pom {
                name.set("$pluginName-api")
                description.set("Public API for $pluginName (PaperMC)")
                url.set(repoUrl)

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0 or later")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
                    }
                }

                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                    }
                }

                scm {
                    url.set(repoUrl)
                    connection.set("scm:git:$repoUrl.git")
                    developerConnection.set("scm:git:$repoUrl.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "YuemiMaven"
            val isSnapshot = project.version.toString().endsWith("SNAPSHOT")
            url = uri(if (isSnapshot) yuemiMavenSnapshot else yuemiMavenRelease)

            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
