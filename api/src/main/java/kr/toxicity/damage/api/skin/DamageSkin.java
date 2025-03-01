package kr.toxicity.damage.api.skin;

import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public interface DamageSkin {
    @NotNull String name();
    @NotNull @Unmodifiable
    Map<DamageTriggerType, DamageEffect> effectMap();
    @Nullable DamageEffect fallback();

    default @Nullable DamageEffect find(@NotNull DamageTriggerType type) {
        var get = effectMap().get(type);
        return get != null ? get : fallback();
    }
}
