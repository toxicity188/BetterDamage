package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.skin.DamageSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigManager extends DamageManager {
    boolean debug();
    boolean metrics();
    @NotNull String namespace();
    @NotNull String packPath();
    @NotNull String packType();
    boolean createMcmeta();
    long autoSaveTime();
    @Nullable DamageEffect defaultEffect();
    @Nullable DamageSkin defaultSkin();
}
