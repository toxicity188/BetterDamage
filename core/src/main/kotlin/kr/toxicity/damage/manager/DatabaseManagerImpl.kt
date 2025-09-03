package kr.toxicity.damage.manager

import kr.toxicity.damage.api.database.DamagePlayerDatabase
import kr.toxicity.damage.api.manager.DatabaseManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.config.PluginConfig
import kr.toxicity.damage.database.MysqlDatabase
import kr.toxicity.damage.database.YamlDatabase
import org.bukkit.configuration.file.YamlConfiguration

object DatabaseManagerImpl : DatabaseManager {

    private val defaultDatabase = YamlDatabase()
    private var currentDatabase: DamagePlayerDatabase = defaultDatabase
    private var connection = defaultDatabase.connect(YamlConfiguration())
    private val databaseMap = hashMapOf(
        "yml" to defaultDatabase,
        "mysql" to MysqlDatabase()
    )

    override fun defaultDatabase(): DamagePlayerDatabase = defaultDatabase
    override fun connection(): DamagePlayerDatabase.Connection = connection

    override fun addDatabase(name: String, database: DamagePlayerDatabase) {
        databaseMap[name] = database
    }

    override fun reload(assets: PackAssets) {
        PluginConfig.DATABASE.load().run {
            val type = getString("type")?.let { databaseMap[it] } ?: return
            val data = getConfigurationSection("data") ?: YamlConfiguration()
            if (type === currentDatabase) return
            currentDatabase = type
            connection.close()
            connection = currentDatabase.connect(data)
        }
    }

    override fun end() {
        connection.close()
    }
}