package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.PlayerManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.player.DamagePlayer
import kr.toxicity.damage.player.DamagePlayerImpl
import kr.toxicity.damage.util.PLUGIN
import kr.toxicity.damage.util.async
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerManagerImpl : PlayerManager {

    private val playerMap = ConcurrentHashMap<UUID, DamagePlayerImpl>()

    override fun start() {
        fun Player.register() {
            playerMap.computeIfAbsent(uniqueId) {
                DamagePlayerImpl(this)
            }
        }
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun PlayerJoinEvent.join() {
                async {
                    player.register()
                }
            }
            @EventHandler
            fun PlayerQuitEvent.quit() {
                playerMap.remove(player.uniqueId)?.let {
                    it.cancel()
                    async {
                        it.save()
                    }
                }
            }
        }, PLUGIN)
        Bukkit.getOnlinePlayers().forEach {
            it.register()
        }
    }

    override fun reload(assets: PackAssets) {
        playerMap.values.forEach(DamagePlayerImpl::reload)
    }

    override fun end() {
        playerMap.values.forEach(DamagePlayer::save)
        playerMap.clear()
    }

    override fun player(uuid: UUID): DamagePlayer? = playerMap[uuid]
}