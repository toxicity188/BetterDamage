package kr.toxicity.damage.util

import kr.toxicity.damage.api.equation.TEquation
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.Reader

fun File.toYaml() = YamlConfiguration.loadConfiguration(this)
fun Reader.toYaml() = YamlConfiguration.loadConfiguration(this)

fun ConfigurationSection.subConfiguration(block: (String, ConfigurationSection) -> Unit) {
    getKeys(false).forEach { key ->
        getConfigurationSection(key)?.let { section ->
            block(key, section)
        }
    }
}

fun ConfigurationSection.getAsEquation(key: String) = getString(key)?.let { TEquation(it) }
fun ConfigurationSection.getFilePath(key: String) = getString(key)?.toFilePath()