package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.database.DamagePlayerDatabase;
import org.jetbrains.annotations.NotNull;

/**
 * Database manager
 */
public interface DatabaseManager extends DamageManager {
    /**
     * Gets default database
     * @return default database
     */
    @NotNull DamagePlayerDatabase defaultDatabase();

    /**
     * Gets current connection
     * @return current connection
     */
    @NotNull DamagePlayerDatabase.Connection connection();

    /**
     * Adds some database to this registry
     * @param name name
     * @param database database
     */
    void addDatabase(@NotNull String name, @NotNull DamagePlayerDatabase database);
}
