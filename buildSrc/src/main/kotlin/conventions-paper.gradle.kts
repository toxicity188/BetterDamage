plugins {
    id("conventions-standard")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${property("minecraft_version")}.build.+")
    compileOnly(libs.bundles.library.shaded)
}