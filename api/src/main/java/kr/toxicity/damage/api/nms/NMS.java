package kr.toxicity.damage.api.nms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * A version-volatile code (net.minecraft)
 */
public interface NMS {
    /**
     * Gets NMS version
     * @return version
     */
    @NotNull NMSVersion version();

    /**
     * Creates damage display at some location
     * @param location initial location
     * @return display
     */
    @NotNull DamageDisplay create(@NotNull Location location);
}
