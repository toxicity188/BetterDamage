package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.effect.DamageEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface EffectManager extends DamageManager {
    @NotNull @Unmodifiable
    Set<String> effectNames();
    @Nullable DamageEffect effect(@NotNull String name);
}
