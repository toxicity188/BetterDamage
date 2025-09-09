package kr.toxicity.damage.database

import kr.toxicity.damage.api.database.DamagePlayerDatabase
import kr.toxicity.damage.api.player.DamagePlayerData
import kr.toxicity.damage.manager.DamageSkinManagerImpl
import kr.toxicity.damage.player.DamagePlayerDataImpl
import kr.toxicity.damage.util.DATA_FOLDER
import kr.toxicity.damage.util.subFolder
import kr.toxicity.damage.util.toYaml
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class YamlDatabase : DamagePlayerDatabase {
    override fun connect(section: ConfigurationSection): DamagePlayerDatabase.Connection {
        return YamlConnection()
    }

    private class YamlConnection : DamagePlayerDatabase.Connection {

        private val dir
            get() = DATA_FOLDER.subFolder(".users")

        private fun UUID.load() = File(dir, "$this.yml")

        override fun load(uuid: UUID): DamagePlayerData {
            return uuid.load().toYaml().run {
                DamagePlayerDataImpl(
                    getString("selected")?.let { DamageSkinManagerImpl.skin(it) },
                    getStringList("skins")
                        .asSequence()
                        .mapNotNull {
                            DamageSkinManagerImpl.skin(it)
                        }.associate {
                            it.name() to it
                        }.toMutableMap()
                )
            }
        }

        override fun save(uuid: UUID, data: DamagePlayerData) {
            YamlConfiguration().run {
                set("selected", data.selectedSkin()?.name())
                set("skins", data.skins().map {
                    it.name()
                }.toTypedArray())
                save(uuid.load())
            }
        }

        override fun close() {
        }
    }
}