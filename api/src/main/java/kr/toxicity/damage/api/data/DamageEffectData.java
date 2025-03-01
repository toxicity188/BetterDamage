package kr.toxicity.damage.api.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record DamageEffectData(@NotNull Player player, @NotNull Entity entity, double damage) {
}
