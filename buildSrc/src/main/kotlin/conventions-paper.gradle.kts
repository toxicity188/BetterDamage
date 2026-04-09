plugins {
    id("conventions-standard")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.1.build.+")
    compileOnly(libs.bundles.library.shaded)
}