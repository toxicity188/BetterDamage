package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.CompatibilityManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.compatibility.mythiclib.MythicLibCompatibility
import kr.toxicity.damage.compatibility.nexo.NexoCompatibility
import kr.toxicity.damage.util.info
import org.bukkit.Bukkit

object CompatibilityManagerImpl : CompatibilityManager {

    private val hookMap = mapOf(
        "MythicLib" to {
            MythicLibCompatibility()
        },
        "Nexo" to {
            NexoCompatibility()
        }
    )

    override fun start() {
        Bukkit.getPluginManager().run {
            info(*hookMap.mapNotNull { (name, hooker) ->
                if (isPluginEnabled(name)) {
                    hooker().start()
                    "Plugin hooks $name."
                } else null
            }.toTypedArray())
        }
    }

    override fun reload(assets: PackAssets) {
    }
}