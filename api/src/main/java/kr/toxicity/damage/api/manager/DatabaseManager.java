package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.database.DamagePlayerDatabase;
import org.jetbrains.annotations.NotNull;

public interface DatabaseManager extends DamageManager {
    @NotNull DamagePlayerDatabase defaultDatabase();
    @NotNull DamagePlayerDatabase.Connection connection();
    void addDatabase(@NotNull String name, @NotNull DamagePlayerDatabase database);
}
