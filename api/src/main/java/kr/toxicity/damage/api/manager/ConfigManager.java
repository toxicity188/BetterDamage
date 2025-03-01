package kr.toxicity.damage.api.manager;

import org.jetbrains.annotations.NotNull;

public interface ConfigManager extends DamageManager {
    boolean debug();
    boolean metrics();
    @NotNull String namespace();
    @NotNull String packPath();
    @NotNull String packType();
    boolean createMcmeta();
    long autoSaveTime();
    @NotNull String defaultEffect();
}
