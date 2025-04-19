package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.pack.PackAssets;
import org.jetbrains.annotations.NotNull;

/**
 * BetterDamage's manager
 */
public interface DamageManager {
    /**
     * Executes on load
     */
    default void load() {}

    /**
     * Executes on enable
     */
    default void start() {}

    /**
     * Executes on reload
     * @param assets pack assets
     */
    void reload(@NotNull PackAssets assets);

    /**
     * Executes on disable
     */
    default void end() {}
}