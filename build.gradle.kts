subprojects {
    apply(plugin = "java")

    group = property("group") as String
    version = property("version") as String

    // Configure java extension safely
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.yuemi.my.id/repository/maven-public/")
        maven("https://repo.momirealms.net/releases/")
        maven("https://jitpack.io")
        maven("https://repo.dmulloy2.net/repository/public/")
        maven("https://repo.nexomc.com/releases/")
        maven("https://repo.triumphteam.dev/snapshots/")
        maven("https://repo.devs.beer/repository/maven-public/")
    }

    dependencies {
        val paperApiVersion = property("paperApiVersion") as String
        add("compileOnly", "io.papermc.paper:paper-api:$paperApiVersion")
        add("testImplementation", "io.papermc.paper:paper-api:$paperApiVersion")

        // Testing frameworks
        add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.10.2")
        add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.10.2")
        add("testImplementation", "org.mockito:mockito-core:5.11.0")
        add("testImplementation", "org.mockito:mockito-junit-jupiter:5.11.0")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
