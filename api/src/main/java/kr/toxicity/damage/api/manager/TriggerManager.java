package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.trigger.DamageTrigger;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Trigger manager
 */
public interface TriggerManager extends DamageManager {
    /**
     * Gets the trigger type by raw string
     * @param name name
     * @return type
     */
    @Nullable DamageTriggerType type(@NotNull String name);

    /**
     * Adds some trigger to this registry
     * @param type type
     */
    void addTrigger(@NotNull DamageTrigger<?> type);
}
