package kr.toxicity.damage.api.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface DamagePlayer {
    @NotNull Player player();
    @NotNull DamagePlayerData data();

    void save();
    void cancel();
}
