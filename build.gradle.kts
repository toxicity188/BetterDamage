import java.time.LocalDateTime

plugins {
    java
    kotlin("jvm") version "2.2.20"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
    id("xyz.jpenilla.run-paper") version "3.0.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.1"
    id("com.gradleup.shadow") version "9.2.2"
}

val minecraft = "1.21.8"
val targetJavaVersion = 21
val adventure = "4.24.0"

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    group = "kr.toxicity.damage"
    version = "1.2.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://nexus.phoenixdevt.fr/repository/maven-public/") //MMOItems, MMOCore, MythicLib
        maven("https://jitpack.io")
        maven("https://mvn.lumine.io/repository/maven-public/")
        maven("https://repo.momirealms.net/releases/")
        maven("https://repo.nexomc.com/releases/")
    }

    dependencies {
        testImplementation(kotlin("test"))
        implementation("dev.jorel:commandapi-bukkit-shade:10.1.2")
        implementation(rootProject.fileTree("libs"))
        implementation("org.bstats:bstats-bukkit:3.1.0")
        implementation("com.github.toxicity188:SharedPackets:1.0.0") {
            exclude("net.kyori")
        }

        compileOnly("com.zaxxer:HikariCP:7.0.2")
        compileOnly("com.vdurmont:semver4j:3.1.0")
        compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
        compileOnly("net.jodah:expiringmap:0.5.11")
        compileOnly("net.objecthunter:exp4j:0.4.8")

        compileOnly("io.github.toxicity188:bettermodel:1.13.0")
        compileOnly("io.lumine:MythicLib-dist:1.7.1-SNAPSHOT")
        compileOnly("net.Indyuce:MMOCore-API:1.13.1-SNAPSHOT")
        compileOnly("net.Indyuce:MMOItems-API:6.10.1-SNAPSHOT")
        compileOnly("net.momirealms:craft-engine-core:0.0.63")
        compileOnly("net.momirealms:craft-engine-bukkit:0.0.63")
        compileOnly("com.nexomc:nexo:1.11.0-dev")
    }

    tasks {
        test {
            useJUnitPlatform()
        }
        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }
    }
    java {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    kotlin {
        jvmToolchain(targetJavaVersion)
    }
}

fun Project.dependency(any: Any) = also { project ->
    if (any is Collection<*>) {
        any.forEach {
            if (it == null) return@forEach
            project.dependencies {
                compileOnly(it)
                testImplementation(it)
            }
        }
    } else {
        project.dependencies {
            compileOnly(any)
            testImplementation(any)
        }
    }
}

fun Project.paper() = dependency("io.papermc.paper:paper-api:$minecraft-R0.1-SNAPSHOT")

val api = project("api").paper()
val nms = project("nms").subprojects.map {
    it.dependency(api)
        .also { project ->
            project.apply(plugin = "io.papermc.paperweight.userdev")
        }
}
val modelEngine = project("modelengine").subprojects.map {
    it.dependency(api).paper()
}
val core = project("core")
    .paper()
    .dependency(api)
    .dependency(modelEngine)
    .dependency(nms)

dependencies {
    implementation(api)
    implementation(core)
    modelEngine.forEach {
        implementation(it)
    }
    nms.forEach {
        implementation(project("nms:${it.name}", configuration = "reobf"))
    }
}

val pluginVersion = version.toString()
val groupString = group.toString()

tasks {
    runServer {
        pluginJars(fileTree("plugins"))
        version(minecraft)
        downloadPlugins {
            hangar("ViaVersion", "5.4.2")
            hangar("ViaBackwards", "5.4.2")
        }
    }
    jar {
        finalizedBy(shadowJar)
    }
    shadowJar {
        manifest {
            attributes(
                "paperweight-mappings-namespace" to "spigot",
                "Version" to pluginVersion,
                "Author" to "toxicity188",
                "Url" to "https://github.com/toxicity188/BetterDamage",
                "Created-By" to "Gradle $gradle",
                "Build-Jdk" to "${System.getProperty("java.vendor")} ${System.getProperty("java.version")}",
                "Build-OS" to "${System.getProperty("os.arch")} ${System.getProperty("os.name")}",
                "Build-Date" to LocalDateTime.now().toString()
            )
        }
        archiveClassifier = ""
        dependencies {
            exclude(dependency("org.jetbrains:annotations:26.0.2"))
        }
        fun prefix(pattern: String) {
            relocate(pattern, "$groupString.shaded.$pattern")
        }
        exclude("LICENSE")
        prefix("kotlin")
        prefix("dev.jorel.commandapi")
        prefix("org.bstats")
        prefix("kr.toxicity.library")
    }
}

bukkitPluginYaml {
    main = "${project.group}.BetterDamagePluginImpl"
    version = project.version.toString()
    website = "https://github.com/toxicity188/BetterDamage"
    name = rootProject.name
    foliaSupported = true
    apiVersion = "1.20"
    author = "toxicity"
    description = "Provides simple damage skin for Minecraft Bukkit."
    softDepend = listOf(
        "BetterModel",
        "MMOCore",
        "MMOItems",
        "ModelEngine",
        "CraftEngine",
        "Nexo"
    )
    libraries = listOf(
        "net.kyori:adventure-api:$adventure",
        "net.kyori:adventure-text-serializer-gson:$adventure",
        "net.kyori:adventure-platform-bukkit:4.4.1",
        "com.zaxxer:HikariCP:7.0.2",
        "com.vdurmont:semver4j:3.1.0",
        "net.jodah:expiringmap:0.5.11",
        "net.objecthunter:exp4j:0.4.8"
    )
}