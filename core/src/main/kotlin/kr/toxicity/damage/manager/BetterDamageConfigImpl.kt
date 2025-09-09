package kr.toxicity.damage.manager

import kr.toxicity.damage.api.BetterDamageConfig
import kr.toxicity.damage.api.effect.DamageEffect
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.util.getFilePath
import kr.toxicity.damage.util.memoizeNullable
import org.bukkit.configuration.ConfigurationSection

class BetterDamageConfigImpl(yaml: ConfigurationSection) : BetterDamageConfig {

    private val debug = yaml.getBoolean("debug")
    private val metrics = yaml.getBoolean("metrics", true)
    private val namespace = yaml.getString("namespace") ?: "betterdamage"
    private val packPath = yaml.getFilePath("pack-path") ?: "BetterDamage/build"
    private val packType = yaml.getString("pack-type") ?: "zip"
    private val autoSaveTime = yaml.getLong("auto-save-time", 300) * 20
    private val skipZeroDamage = yaml.getBoolean("skip-zero-damage", true)
    private val effectSupplier = (yaml.getString("default-effect") ?: "default_effect").let {
        {
            EffectManagerImpl.effect(it)
        }.memoizeNullable()
    }
    private val skinSupplier = (yaml.getString("default-skin") ?: "default_skin").let {
        {
            DamageSkinManagerImpl.skin(it)
        }.memoizeNullable()
    }
    private val mergeWithExternalResources = yaml.getBoolean("merge-with-external-resources", true)

    override fun debug(): Boolean = debug
    override fun namespace(): String = namespace
    override fun packPath(): String = packPath
    override fun packType(): String = packType
    override fun autoSaveTime(): Long = autoSaveTime
    override fun skipZeroDamage(): Boolean = skipZeroDamage
    override fun metrics(): Boolean = metrics
    override fun defaultEffect(): DamageEffect? = effectSupplier()
    override fun defaultSkin(): DamageSkin? = skinSupplier()
    override fun mergeWithExternalResources(): Boolean = mergeWithExternalResources
}