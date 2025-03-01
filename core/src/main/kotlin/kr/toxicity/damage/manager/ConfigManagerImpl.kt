package kr.toxicity.damage.manager

import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.manager.ConfigManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.pack.PackPath
import kr.toxicity.damage.api.pack.PackSupplier
import kr.toxicity.damage.config.PluginConfig
import kr.toxicity.damage.util.PLUGIN
import kr.toxicity.damage.util.getFilePath
import kr.toxicity.damage.util.jsonObjectOf
import kr.toxicity.damage.util.toFilePath
import org.bstats.bukkit.Metrics

object ConfigManagerImpl : ConfigManager {

    private var debug = false
    private var metrics: Metrics? = null
    private var namespace = "betterdamage"
    private var packPath = "BetterDamage/build".toFilePath()
    private var packType = "zip"
    private var createMcmeta = false
    private var autoSaveTime = 300L * 20
    private var defaultEffect = "default_effect"

    override fun reload(assets: PackAssets) {
        PluginConfig.CONFIG.load().run {
            debug = getBoolean("debug")
            if (getBoolean("metrics", true) && metrics == null) {
                metrics = Metrics(PLUGIN, BetterDamage.BSTATS_ID)
            } else {
                metrics?.shutdown()
                metrics = null
            }
            namespace = getString("namespace") ?: "betterdamage"
            packPath = getFilePath("pack-path") ?: "BetterDamage/build".toFilePath()
            packType = getString("pack-type") ?: "zip"
            createMcmeta = getBoolean("create-mcmeta")
            autoSaveTime = getLong("auto-save-time", 300) * 20
            defaultEffect = getString("default-effect") ?: "default_effect"
        }
        assets.add(PackPath("pack.mcmeta"), PackSupplier.of(jsonObjectOf(
            "pack" to jsonObjectOf(
                "pack_format" to PLUGIN.nms().version().metaVersion,
                "description" to "BetterDamage's resource pack."
            )
        )))
    }

    override fun debug(): Boolean = debug
    override fun namespace(): String = namespace
    override fun packPath(): String = packPath
    override fun packType(): String = packType
    override fun createMcmeta(): Boolean = createMcmeta
    override fun autoSaveTime(): Long = autoSaveTime
    override fun defaultEffect(): String = defaultEffect
    override fun metrics(): Boolean = metrics != null
}