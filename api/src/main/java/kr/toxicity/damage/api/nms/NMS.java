package kr.toxicity.damage.api.nms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * A version-volatile code (net.minecraft)
 */
public interface NMS {
    @NotNull NMSVersion version();
    @NotNull DamageDisplay create(@NotNull Location location);
}
