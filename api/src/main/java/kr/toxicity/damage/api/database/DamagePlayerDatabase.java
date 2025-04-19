package kr.toxicity.damage.api.database;

import kr.toxicity.damage.api.player.DamagePlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Player database
 */
public interface DamagePlayerDatabase {

    /**
     * Creates a connection by given config
     * @param section config
     * @return connection
     */
    @NotNull Connection connect(@NotNull ConfigurationSection section);

    /**
     * Connection
     */
    interface Connection extends AutoCloseable {
        /**
         * Loads player data
         * @param uuid player's uuid
         * @return player data
         * @throws Exception connection exception
         */
        @NotNull DamagePlayerData load(@NotNull UUID uuid) throws Exception;

        /**
         * Saves player data
         * @param uuid player's uuid
         * @param data player data
         * @throws Exception connection exception
         */
        void save(@NotNull UUID uuid, @NotNull DamagePlayerData data) throws Exception;
    }
}
