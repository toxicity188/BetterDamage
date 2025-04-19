package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.skin.DamageSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

/**
 * Damage skin manager
 */
public interface DamageSkinManager extends DamageManager {

    /**
     * Gets damage skin by name
     * @param name name
     * @return damage skin or null
     */
    @Nullable DamageSkin skin(@NotNull String name);

    /**
     * Gets all names of damage skin
     * @return all names
     */
    @NotNull @Unmodifiable
    Set<String> allNames();
}
