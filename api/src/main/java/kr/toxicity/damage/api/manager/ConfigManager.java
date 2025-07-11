package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.skin.DamageSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Config manager
 */
public interface ConfigManager extends DamageManager {
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
     * Checks generates should generate pack.mcmeta
     * @return create mcmeta
     */
    boolean createMcmeta();

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
}
