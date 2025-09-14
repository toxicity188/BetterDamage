package kr.toxicity.damage.compatibility.craftengine

import kr.toxicity.damage.api.ReloadState
import kr.toxicity.damage.compatibility.Compatibility
import kr.toxicity.damage.util.*
import net.momirealms.craftengine.bukkit.api.event.AsyncResourcePackCacheEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CraftEngineCompatibility : Compatibility {
    override fun start() {
        if (CONFIG.mergeWithExternalResources()) PLUGIN.skipInitialReload = true
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun AsyncResourcePackCacheEvent.generate() {
                if (!CONFIG.mergeWithExternalResources()) return
                when (val result = PLUGIN.reload()) {
                    is ReloadState.Success -> {
                        val dir = result.directory()
                        when {
                            dir.isFile -> cacheData().externalZips().add(dir.toPath())
                            dir.isDirectory -> cacheData().externalFolders().add(dir.toPath())
                        }
                        info("Successfully merged with CraftEngine.")
                    }
                    is ReloadState.OnReload -> {
                        warn("BetterDamage is still on reload!")
                    }
                    is ReloadState.Failure -> {
                        result.throwable.handle("Unable to merge with CraftEngine.")
                    }
                }
            }
        }, PLUGIN)
    }
}