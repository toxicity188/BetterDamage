plugins {
    id("conventions-standard")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.112.+")
    compileOnly(libs.bundles.library.shaded)
}