package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.pack.PackAssets;
import org.jetbrains.annotations.NotNull;

public interface DamageManager {
    default void load() {}
    default void start() {}
    void reload(@NotNull PackAssets assets);
    default void end() {}
}