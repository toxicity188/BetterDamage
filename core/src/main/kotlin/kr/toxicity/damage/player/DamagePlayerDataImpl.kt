package kr.toxicity.damage.player

import kr.toxicity.damage.api.player.DamagePlayerData
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.manager.DamageSkinManagerImpl
import org.jetbrains.annotations.Unmodifiable
import java.util.*

class DamagePlayerDataImpl(
    private var selectedSkin: DamageSkin?,
    private var skins: MutableMap<String, DamageSkin>
) : DamagePlayerData {
    private val unmodifiableRef = Collections.unmodifiableMap(skins)

    val addSet = mutableSetOf<String>()
    val removalSet = mutableSetOf<String>()

    override fun skins(): @Unmodifiable Collection<DamageSkin> = unmodifiableRef.values

    @Synchronized
    fun reload() {
        skins = skins.keys.mapNotNull {
            DamageSkinManagerImpl.skin(it)
        }.associateBy {
            it.name()
        }.toMutableMap()
        selectedSkin = selectedSkin?.let {
            DamageSkinManagerImpl.skin(it.name())
        }
    }

    @Synchronized
    override fun add(skin: DamageSkin): Boolean {
        return if (skins.put(skin.name(), skin) == null) {
            if (!removalSet.remove(skin.name())) addSet += skin.name()
            true
        } else false
    }

    @Synchronized
    override fun remove(skin: DamageSkin): Boolean {
        return if (skins.remove(skin.name()) != null) {
            if (!addSet.remove(skin.name())) removalSet += skin.name()
            true
        } else false
    }

    @Synchronized
    override fun select(skin: DamageSkin): Boolean {
        return if (skins.containsKey(skin.name())) {
            selectedSkin = skin
            true
        } else false
    }

    @Synchronized
    override fun selectedSkin(): DamageSkin? = selectedSkin
}