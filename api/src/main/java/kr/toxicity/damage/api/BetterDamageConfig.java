package kr.toxicity.damage.api;

import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.skin.DamageSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Config manager
 */
public interface BetterDamageConfig {
    /**
     * Checks debug mode is enabled.
     * @return debug
     */
    boolean debug();

    /**
     * Checks metrics is enabled.
     * @return metrics
     */
    boolean metrics();

    /**
     * Gets pack namespace (assets/namespace)
     * Default: betterdamage
     * @return namespace
     */
    @NotNull String namespace();

    /**
     * Gets pack directory location
     * Default: BetterDamage/build
     * @return path
     */
    @NotNull String packPath();

    /**
     * Gets generator's type
     * Default: zip
     * @return type
     */
    @NotNull String packType();

    /**
     * Gets player's data auto save time (second)
     * Default: 300
     * @return auto save time
     */
    long autoSaveTime();

    /**
     * Skips if event-damage is zero
     * @return skip zero damage
     */
    boolean skipZeroDamage();

    /**
     * Gets default damage effect
     * @return default damage effect
     */
    @Nullable DamageEffect defaultEffect();

    /**
     * Gets default damage skin
     * @return default damage skin
     */
    @Nullable DamageSkin defaultSkin();

    /**
     * Checks BetterDamage should try merging resource pack with external plugin.
     * @return merge with external resources.
     */
    boolean mergeWithExternalResources();

}
