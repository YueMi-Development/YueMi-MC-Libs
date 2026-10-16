import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask

plugins {
    id("com.github.spotbugs") apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.spotbugs")

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
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    dependencies {
        val paperApiVersion = property("paperApiVersion") as String
        val spotbugsToolVersion = property("spotbugsToolVersion") as String
        add("compileOnly", "io.papermc.paper:paper-api:$paperApiVersion")
        add("testImplementation", "io.papermc.paper:paper-api:$paperApiVersion")
        add("compileOnly", "me.clip:placeholderapi:2.11.6")
        add("compileOnly", "com.github.spotbugs:spotbugs-annotations:$spotbugsToolVersion")

        // Testing frameworks
        add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.10.2")
        add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.10.2")
        add("testImplementation", "org.mockito:mockito-core:5.11.0")
        add("testImplementation", "org.mockito:mockito-junit-jupiter:5.11.0")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    extensions.configure<SpotBugsExtension> {
        toolVersion.set(property("spotbugsToolVersion") as String)
        effort.set(Effort.MAX)
        reportLevel.set(Confidence.LOW)
        ignoreFailures.set(false)
        excludeFilter.set(rootProject.file("config/spotbugs/exclude.xml"))
        maxHeapSize.set("1g")
    }

    tasks.withType<SpotBugsTask>().configureEach {
        // Test sources are not analyzed: they mock Bukkit/Paper APIs and produce false positives.
        if (name.endsWith("Test")) {
            enabled = false
        }

        // The reports container is empty by default, so reports must be requested explicitly.
        reports.create("html") {
            required.set(true)
            setStylesheet("fancy-hist.xsl")
        }
        reports.create("xml") {
            required.set(true)
        }
    }
}
