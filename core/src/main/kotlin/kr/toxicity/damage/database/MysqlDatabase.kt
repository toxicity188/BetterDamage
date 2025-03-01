package kr.toxicity.damage.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kr.toxicity.damage.api.database.DamagePlayerDatabase
import kr.toxicity.damage.api.player.DamagePlayerData
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.manager.DamageSkinManagerImpl
import kr.toxicity.damage.player.DamagePlayerDataImpl
import kr.toxicity.damage.util.ifNull
import org.bukkit.configuration.ConfigurationSection
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID
import kotlin.use

class MysqlDatabase : DamagePlayerDatabase {
    override fun connect(section: ConfigurationSection): DamagePlayerDatabase.Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val host = section.getString("host").ifNull { "unable to find the host value." }
        val database = section.getString("database").ifNull { "unable to find the database value." }
        return MysqlConnection(HikariDataSource(HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://$host/$database"
            username = section.getString("name").ifNull { "unable to find the name value." }
            password = section.getString("password").ifNull { "unable to find the password value." }
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 30000
            maxLifetime = 1800000
            validationTimeout = 5000
            connectionTestQuery = "SELECT 1"
        }).apply {
            connection.use {
                it.createStatement().use { s ->
                    s.execute("CREATE TABLE IF NOT EXISTS player_selected_skin (uuid CHAR(36), skin VARCHAR(255) NOT NULL, PRIMARY KEY(uuid));")
                    s.execute("CREATE TABLE IF NOT EXISTS player_skin (uuid CHAR(36), skin VARCHAR(255), PRIMARY KEY(uuid, skin));")
                }
            }
        })
    }

    private class MysqlConnection(
        private val pool: HikariDataSource
    ) : DamagePlayerDatabase.Connection {

        private fun <T> Connection.query(query: String, setter: PreparedStatement.() -> Unit, mapper: ResultSet.() -> T) = prepareStatement(query).use {
            it.setter()
            it.executeQuery().use { result ->
                result.mapper()
            }
        }
        private fun Connection.update(query: String, setter: PreparedStatement.() -> Unit = {}) = prepareStatement(query).use {
            it.setter()
            it.executeUpdate()
        }

        private fun <T> transaction(connection: Connection.() -> T) = pool.connection.use {
            connection(it)
        }
        private fun Iterable<*>.toPlaceholder(amount: Int = 1) = joinToString(",") { "(" + (0..<amount).joinToString(",") { "?" } + ")" }

        override fun load(uuid: UUID): DamagePlayerData {
            val uuidString = uuid.toString()
            return transaction {
                DamagePlayerDataImpl(
                    query("SELECT skin FROM player_selected_skin WHERE uuid = ?;", {
                        setString(1, uuidString)
                    }) {
                        if (next()) DamageSkinManagerImpl.skin(getString("skin")) else null
                    },
                    query("SELECT skin FROM player_skin WHERE uuid = ?;", {
                        setString(1, uuidString)
                    }) {
                        val map = hashMapOf<String, DamageSkin>()
                        while (next()) {
                            DamageSkinManagerImpl.skin(getString("skin"))?.let { skin ->
                                map[skin.name()] = skin
                            }
                        }
                        map
                    }
                )
            }
        }

        override fun save(uuid: UUID, data: DamagePlayerData) {
            val impl = data as DamagePlayerDataImpl
            val uuidString = uuid.toString()
            transaction {
                if (impl.removalSet.isNotEmpty()) update("DELETE FROM player_skin WHERE uuid = ? AND skin IN (${impl.removalSet.toPlaceholder()});") {
                    setString(1, uuidString)
                    impl.removalSet.forEachIndexed { index, string ->
                        setString(index + 2, string)
                    }
                }
                if (impl.addSet.isNotEmpty()) update("INSERT INTO player_skin (uuid, skin) VALUES ${impl.addSet.toPlaceholder(2)};") {
                    impl.addSet.forEachIndexed { index, string ->
                        setString(index * 2 + 1, uuidString)
                        setString(index * 2 + 2, string)
                    }
                }
                impl.selectedSkin()?.let {
                    update("INSERT INTO player_selected_skin (uuid, skin) VALUES (?, ?) ON DUPLICATE KEY UPDATE skin = ?;") {
                        setString(1, uuidString)
                        setString(2, it.name())
                        setString(3, it.name())
                    }
                } ?: update("DELETE FROM player_selected_skin WHERE uuid = ?;") {
                    setString(1, uuidString)
                }
                impl.removalSet.clear()
                impl.addSet.clear()
            }
        }

        override fun close() {
            pool.close()
        }
    }
}