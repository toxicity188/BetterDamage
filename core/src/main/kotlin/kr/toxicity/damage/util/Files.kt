package kr.toxicity.damage.util

import org.bukkit.configuration.ConfigurationSection
import java.io.File

fun File.subFolder(dir: String) = File(this, dir).apply {
    mkdirs()
}

fun File.forEach(block: (File) -> Unit) {
    listFiles()?.forEach(block)
}

fun File.forEachAllFolder(block: (File) -> Unit) {
    if (isDirectory) forEach {
        it.forEachAllFolder(block)
    } else {
        block(this)
    }
}

fun File.ifEmpty(message: File.() -> String) = apply {
    if (!exists()) throw RuntimeException(message())
}

fun File.forEachAllYaml(block: (String, ConfigurationSection) -> Unit) {
    forEachAllFolder {
        if (it.extension == "yml") {
            runCatching {
                it.toYaml().subConfiguration(block)
            }.onFailure { e ->
                e.handle("Unable to load this file: ${it.name}")
            }
        }
    }
}