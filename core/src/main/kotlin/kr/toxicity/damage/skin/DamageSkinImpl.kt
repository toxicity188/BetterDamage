package kr.toxicity.damage.skin

import kr.toxicity.damage.api.effect.DamageEffect
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.api.trigger.DamageTriggerType
import kr.toxicity.damage.manager.EffectManagerImpl
import kr.toxicity.damage.manager.TriggerManagerImpl
import kr.toxicity.damage.util.ifNull
import org.bukkit.configuration.ConfigurationSection
import org.jetbrains.annotations.Unmodifiable

class DamageSkinImpl(
    private val name: String,
    section: ConfigurationSection
) : DamageSkin {

    private val effectMap = section.getConfigurationSection("skins")?.run {
        getKeys(false).associate {
            TriggerManagerImpl.type(it).ifNull {
                "This trigger doesn't exist: $it in effect $name"
            } to getString(it).ifNull {
                "This section is not a string: $it in effect $name"
            }.let { s ->
                EffectManagerImpl.effect(s).ifNull {
                    "This effect doesn't exist: $s in effect $name"
                }
            }
        }
    }.ifNull {
        "'skins' configuration doesn't exist in effect $name"
    }

    private val fallback = section.getString("fallback")?.let {
        EffectManagerImpl.effect(it).ifNull { "This effect doesn't exist: $it in effect $name" }
    }

    override fun name(): String = name
    override fun effectMap(): @Unmodifiable Map<DamageTriggerType, DamageEffect> = effectMap
    override fun fallback(): DamageEffect? = fallback

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DamageSkinImpl) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}