package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.trigger.DamageTrigger;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TriggerManager extends DamageManager {
    @Nullable DamageTriggerType type(@NotNull String name);
    void addTrigger(@NotNull DamageTrigger<?> type);
}
