package kr.toxicity.damage.api.database;

import kr.toxicity.damage.api.player.DamagePlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DamagePlayerDatabase {

    @NotNull Connection connect(@NotNull ConfigurationSection section);

    interface Connection extends AutoCloseable {
        @NotNull DamagePlayerData load(@NotNull UUID uuid) throws Exception;
        void save(@NotNull UUID uuid, @NotNull DamagePlayerData data) throws Exception;
    }
}
