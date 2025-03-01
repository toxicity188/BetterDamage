package kr.toxicity.damage.config

import kr.toxicity.damage.util.PLUGIN
import kr.toxicity.damage.util.toYaml
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

enum class PluginConfig(
    val fileName: String
) {
    CONFIG("config.yml"),
    DATABASE("database.yml")
    ;
    fun load(): ConfigurationSection {
        val resource = PLUGIN.getResource(fileName)?.bufferedReader()?.use {
            it.toYaml()
        } ?: YamlConfiguration()
        val data = File(PLUGIN.dataFolder, fileName)
        return (if (data.exists()) {
            data.toYaml().apply {
                resource.getKeys(false).forEach { key ->
                    if (!isSet(key)) {
                        set(key, resource[key])
                        setInlineComments(key, resource.getInlineComments(key))
                        setComments(key, resource.getComments(key))
                    }
                }
            }
        } else resource).apply {
            save(data)
        }
    }
}