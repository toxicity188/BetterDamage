plugins {
    id("conventions-standard")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":api"))
}