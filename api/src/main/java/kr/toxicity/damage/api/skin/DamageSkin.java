package kr.toxicity.damage.api.skin;

import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

/**
 * Damage skin
 */
public interface DamageSkin {
    /**
     * Gets the skin's name
     * @return name
     */
    @NotNull String name();

    /**
     * Gets effect map
     * @return map
     */
    @NotNull @Unmodifiable
    Map<DamageTriggerType, DamageEffect> effectMap();

    /**
     * Gets fallback effect
     * @return fallback effect
     */
    @Nullable DamageEffect fallback();

    /**
     * Finds damage effect by type
     * @param type type
     * @return effect or null
     */
    default @Nullable DamageEffect find(@NotNull DamageTriggerType type) {
        var get = effectMap().get(type);
        return get != null ? get : fallback();
    }
}
