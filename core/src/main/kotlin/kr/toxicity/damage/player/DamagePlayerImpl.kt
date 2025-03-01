package kr.toxicity.damage.player

import kr.toxicity.damage.api.player.DamagePlayer
import kr.toxicity.damage.api.player.DamagePlayerData
import kr.toxicity.damage.manager.ConfigManagerImpl
import kr.toxicity.damage.util.async
import kr.toxicity.damage.util.handle
import kr.toxicity.damage.util.toPlayerData
import org.bukkit.entity.Player

class DamagePlayerImpl(
    private val player: Player,
) : DamagePlayer {

    private val data = player.uniqueId.toPlayerData()

    fun reload() {
        data.reload()
    }

    private val task = async(
        ConfigManagerImpl.autoSaveTime(),
        ConfigManagerImpl.autoSaveTime()
    ) {
        save()
    }

    override fun player(): Player = player
    override fun data(): DamagePlayerData = data

    override fun save() {
        runCatching {
            data.save(player.uniqueId)
        }.onFailure {
            it.handle("Unable to save ${player.name} (${player.uniqueId})'s data.")
        }
    }

    override fun cancel() {
        task.cancel()
    }
}