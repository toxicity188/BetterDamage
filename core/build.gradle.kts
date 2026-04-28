plugins {
    alias(libs.plugins.conventions.core)
}

val nms = project(":nms").subprojects

dependencies {
    compileOnly(project(":modelengine:legacy"))
    compileOnly(project(":modelengine:current"))
    nms.forEach { compileOnly(it) }

    compileOnly("io.github.toxicity188:bettermodel-bukkit-api:3.0.1")
    compileOnly("io.lumine:MythicLib-dist:1.7.1-SNAPSHOT")
    compileOnly("net.Indyuce:MMOCore-API:1.13.1-SNAPSHOT")
    compileOnly("net.Indyuce:MMOItems-API:6.10.1-SNAPSHOT")
    compileOnly("net.momirealms:craft-engine-core:0.0.67")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.67")
    compileOnly("com.nexomc:nexo:1.23")
}