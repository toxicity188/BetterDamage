package kr.toxicity.damage.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Main class of BetterDamage
 * @since 1.0
 */
public final class BetterDamage {

    /**
     * BStats id.
     */
    public static final int BSTATS_ID = 24961;

    /**
     * Check a running platform is Paper.
     */
    public static final boolean IS_PAPER;

    /**
     * Instance
     */
    private static BetterDamagePlugin instance;

    static {
        boolean paper;
        try {
            Class.forName("io.papermc.paper.configuration.PaperConfigurations");
            paper = true;
        } catch (Exception e) {
            paper = false;
        }
        IS_PAPER = paper;
    }

    /**
     * No initializer
     */
    private BetterDamage() {
    }

    /**
     * Gets BetterDamage's plugin instance.
     * @see BetterDamagePlugin
     * @return BetterDamage plugin
     */
    public static @NotNull BetterDamagePlugin inst() {
        return Objects.requireNonNull(instance, "BetterDamage is not initialized yet!");
    }

    /**
     * Sets BetteDamage's instance plugin.
     * @param instance BetterDamage plugin
     */
    @ApiStatus.Internal
    public static void inst(@NotNull BetterDamagePlugin instance) {
        if (BetterDamage.instance != null) throw new RuntimeException("BetterDamage is already initialized.");
        BetterDamage.instance = Objects.requireNonNull(instance, "instance");
    }
}
