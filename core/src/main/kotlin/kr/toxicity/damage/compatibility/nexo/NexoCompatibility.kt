package kr.toxicity.damage.compatibility.nexo

import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent
import kr.toxicity.damage.api.ReloadState
import kr.toxicity.damage.compatibility.Compatibility
import kr.toxicity.damage.util.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NexoCompatibility : Compatibility {
    override fun start() {
        if (CONFIG.mergeWithExternalResources()) PLUGIN.skipInitialReload = true
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun NexoPrePackGenerateEvent.generate() {
                if (!CONFIG.mergeWithExternalResources()) return
                when (val result = PLUGIN.reload()) {
                    is ReloadState.Success -> {
                        addResourcePack(result.directory())
                        info("Successfully merged with Nexo.")
                    }
                    is ReloadState.OnReload -> {
                        warn("BetterModel is still on reload!")
                    }
                    is ReloadState.Failure -> {
                        result.throwable.handle("Unable to merge with Nexo.")
                    }
                }
            }
        }, PLUGIN)
    }
}