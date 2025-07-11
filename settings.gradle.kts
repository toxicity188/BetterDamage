plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "BetterDamage"

include(
    "api",
    "core",

    "nms:v1_20_R1",
    "nms:v1_20_R2",
    "nms:v1_20_R3",
    "nms:v1_20_R4",
    "nms:v1_21_R1",
    "nms:v1_21_R2",
    "nms:v1_21_R3",
    "nms:v1_21_R4",
    "nms:v1_21_R5",

    "modelengine:legacy",
    "modelengine:current",
)