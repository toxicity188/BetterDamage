package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.skin.DamageSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface DamageSkinManager extends DamageManager {
    @Nullable DamageSkin skin(@NotNull String name);
    @NotNull @Unmodifiable
    Set<String> allNames();
}
