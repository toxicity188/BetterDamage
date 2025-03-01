package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.DamageSkinManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.skin.DamageSkinImpl
import kr.toxicity.damage.util.DATA_FOLDER
import kr.toxicity.damage.util.forEachAllYaml
import kr.toxicity.damage.util.handle
import kr.toxicity.damage.util.subFolder
import org.jetbrains.annotations.Unmodifiable
import java.util.Collections

object DamageSkinManagerImpl : DamageSkinManager {

    private val skinMap = hashMapOf<String, DamageSkin>()

    override fun skin(name: String): DamageSkin? = skinMap[name]
    override fun allNames(): @Unmodifiable Set<String> = Collections.unmodifiableSet(skinMap.keys)

    override fun reload(assets: PackAssets) {
        skinMap.clear()
        DATA_FOLDER.subFolder("skins").forEachAllYaml { key, section ->
            runCatching {
                skinMap[key] = DamageSkinImpl(key, section)
            }.onFailure {
                it.handle("Unable to load this damage skin: $key")
            }
        }
    }
}