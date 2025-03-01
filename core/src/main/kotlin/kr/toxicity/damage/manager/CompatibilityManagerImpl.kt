package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.CompatibilityManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.compatibility.mythiclib.MythicLibCompatibility
import kr.toxicity.damage.util.info
import org.bukkit.Bukkit

object CompatibilityManagerImpl : CompatibilityManager {

    private val hookMap = mapOf(
        "MythicLib" to {
            MythicLibCompatibility()
        }
    )

    override fun start() {
        Bukkit.getPluginManager().run {
            hookMap.forEach { (name, hooker) ->
                if (isPluginEnabled(name)) {
                    hooker().start()
                    info("Hooks $name.")
                }
            }
        }
    }

    override fun reload(assets: PackAssets) {
    }
}