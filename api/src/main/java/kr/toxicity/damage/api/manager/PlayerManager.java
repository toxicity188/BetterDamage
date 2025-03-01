package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.player.DamagePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerManager extends DamageManager {
    @Nullable DamagePlayer player(@NotNull UUID uuid);
}
