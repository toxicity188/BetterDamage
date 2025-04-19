package kr.toxicity.damage.api.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Damage effect data
 * @param player caster
 * @param entity target
 * @param damage damage
 */
public record DamageEffectData(@NotNull Player player, @NotNull Entity entity, double damage) {
}
