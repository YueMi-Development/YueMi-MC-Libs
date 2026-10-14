plugins {
    java
}

val pluginVersion: String = project.version.toString()
val paperApiVersion: String by project

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":module-platform"))
    compileOnly("io.papermc.paper:paper-api:${paperApiVersion}")
}
