package kr.toxicity.damage.util

import kr.toxicity.damage.api.player.DamagePlayerData
import kr.toxicity.damage.manager.DatabaseManagerImpl
import kr.toxicity.damage.manager.PlayerManagerImpl
import kr.toxicity.damage.player.DamagePlayerDataImpl
import org.bukkit.OfflinePlayer
import java.util.*

fun UUID.toPlayerData() = DatabaseManagerImpl.connection().load(this) as DamagePlayerDataImpl

fun OfflinePlayer.getPlayerData(block: DamagePlayerData.() -> Unit) {
    uniqueId.getPlayerData(block)
}

fun UUID.getPlayerData(block: DamagePlayerData.() -> Unit) = PlayerManagerImpl.player(this)?.data()?.block() ?: async {
    toPlayerData().block()
}

fun OfflinePlayer.setPlayerData(block: DamagePlayerData.() -> Unit) {
    uniqueId.setPlayerData(block)
}
fun UUID.setPlayerData(block: DamagePlayerData.() -> Unit) = PlayerManagerImpl.player(this)?.run { block(data()) } ?: async {
    toPlayerData().apply(block).save(this@setPlayerData)
}