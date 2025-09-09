package kr.toxicity.damage.api.player;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.skin.DamageSkin;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;

/**
 * Data of player
 */
public interface DamagePlayerData {
    /**
     * Gets selected skin
     * @return selected skin or null
     */
    @Nullable DamageSkin selectedSkin();

    /**
     * Gets all skins
     * @return all skins
     */
    @NotNull @Unmodifiable
    Collection<DamageSkin> skins();

    /**
     * Adds some skin to this data
     * @param skin skin to add
     * @return whether to success or not
     */
    boolean add(@NotNull DamageSkin skin);

    /**
     * Removes some skin to this data
     * @param skin skin to remove
     * @return whether to success or not
     */
    boolean remove(@NotNull DamageSkin skin);

    /**
     * Selects some skin to this data
     * @param skin skin to select
     * @return whether to success or not
     */
    boolean select(@NotNull DamageSkin skin);

    /**
     * Saves this data to some player's uuid
     * @param uuid player's uuid
     * @throws Exception connection exception
     */
    default void save(@NotNull UUID uuid) throws Exception {
        BetterDamage.inst().databaseManager().connection().save(uuid, this);
    }

    /**
     * Gets a proper effect by given type
     * @param type type
     * @return effect or null
     */
    default @Nullable DamageEffect effect(@NotNull DamageTriggerType type) {
        var inst = BetterDamage.inst();
        var selected = selectedSkin();
        if (selected == null) selected = inst.config().defaultSkin();
        if (selected == null) return inst.config().defaultEffect();
        else return selected.find(type);
    }
}
