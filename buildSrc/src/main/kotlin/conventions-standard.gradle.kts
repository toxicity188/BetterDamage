plugins {
    java
    kotlin("jvm")
}

group = "kr.toxicity.damage"
version = property("project_version").toString()

dependencies {
    testImplementation(kotlin("test"))
    compileOnly(libs.bundles.library.standard)
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
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(TARGET_JAVA_VERSION)
}

kotlin {
    jvmToolchain(TARGET_JAVA_VERSION)
}