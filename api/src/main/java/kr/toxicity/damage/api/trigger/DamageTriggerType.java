package kr.toxicity.damage.api.trigger;

import org.jetbrains.annotations.NotNull;

public record DamageTriggerType(@NotNull String name) {
    public static final DamageTriggerType EMPTY = new DamageTriggerType("empty");
}
