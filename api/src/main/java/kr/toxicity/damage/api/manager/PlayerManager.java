package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.player.DamagePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Player manager
 */
public interface PlayerManager extends DamageManager {
    /**
     * Gets a player instance of this player's uuid
     * @param uuid uuid
     * @return player or null
     */
    @Nullable DamagePlayer player(@NotNull UUID uuid);
}
