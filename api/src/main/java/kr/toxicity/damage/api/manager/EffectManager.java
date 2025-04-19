package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.effect.DamageEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

/**
 * Effect manager
 */
public interface EffectManager extends DamageManager {
    /**
     * Gets all names of effect
     * @return effect names
     */
    @NotNull @Unmodifiable
    Set<String> effectNames();

    /**
     * Gets effect by name
     * @param name name
     * @return effect
     */
    @Nullable DamageEffect effect(@NotNull String name);
}
