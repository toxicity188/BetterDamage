package kr.toxicity.damage.api.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Player
 */
public interface DamagePlayer {
    /**
     * Gets Bukkit player
     * @return player
     */
    @NotNull Player player();

    /**
     * Gets player data
     * @return player data
     */
    @NotNull DamagePlayerData data();

    /**
     * Saves player data.
     */
    void save();

    /**
     * Cancels scheduler.
     */
    void cancel();
}
