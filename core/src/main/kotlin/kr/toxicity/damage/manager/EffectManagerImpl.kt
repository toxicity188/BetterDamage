package kr.toxicity.damage.manager

import kr.toxicity.damage.api.effect.DamageEffect
import kr.toxicity.damage.api.manager.EffectManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.effect.DamageEffectImpl
import kr.toxicity.damage.util.DATA_FOLDER
import kr.toxicity.damage.util.forEachAllYaml
import kr.toxicity.damage.util.handle
import kr.toxicity.damage.util.subFolder
import org.jetbrains.annotations.Unmodifiable
import java.util.*

object EffectManagerImpl : EffectManager {

    private val effectMap = hashMapOf<String, DamageEffect>()

    override fun reload(assets: PackAssets) {
        effectMap.clear()
        DATA_FOLDER.subFolder("effects").forEachAllYaml { key, section ->
            runCatching {
                effectMap[key] = DamageEffectImpl(section)
            }.onFailure { e ->
                e.handle("Unable to read this effect: $key")
            }
        }
    }

    override fun effectNames(): @Unmodifiable Set<String> = Collections.unmodifiableSet(effectMap.keys)
    override fun effect(name: String): DamageEffect? = effectMap[name]
}