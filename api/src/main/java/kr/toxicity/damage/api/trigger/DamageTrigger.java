package kr.toxicity.damage.api.trigger;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.data.DamageEffectData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Damage trigger
 * @param <T> event type
 */
public interface DamageTrigger<T extends Event> {

    /**
     * Shared listener
     */
    Listener LISTENER = new Listener() {
    };

    /**
     * Creates the builder of trigger
     * @param eventClass event class
     * @return builder
     * @param <C> event type
     */
    static <C extends Event> @NotNull Builder<C> builder(@NotNull Class<C> eventClass) {
        return new Builder<>(eventClass);
    }

    /**
     * Gets a trigger type
     * @return type
     */
    @NotNull DamageTriggerType type();

    /**
     * Gets an event class
     * @return event class
     */
    @NotNull Class<T> eventClass();

    /**
     * Gets a mapper of event to effect data
     * @return mapper
     */
    @NotNull Function<T, DamageEffectData> mapper();

    /**
     * Registers this trigger
     */
    default void register() {
        Bukkit.getPluginManager().registerEvent(
                eventClass(),
                LISTENER,
                EventPriority.MONITOR,
                (listener, event) -> {
                    if (!eventClass().isInstance(event)) return;
                    var data = mapper().apply(eventClass().cast(event));
                    if (data == null) return;
                    if (BetterDamage.inst().configManager().skipZeroDamage() && data.damage() < 0.5) return;
                    Optional.ofNullable(BetterDamage.inst().playerManager().player(data.player().getUniqueId()))
                            .map(d -> d.data().effect(type()))
                            .ifPresent(effect -> effect.play(data));
                },
                (JavaPlugin) BetterDamage.inst(),
                true
        );
    }

    /**
     * Builder class of trigger
     * @param <T> event type
     */
    @RequiredArgsConstructor
    class Builder<T extends Event> {
        private final Class<T> eventClass;
        private DamageTriggerType type;
        private Function<T, DamageEffectData> mapper;

        /**
         * Sets trigger type
         * @param type type
         * @return self
         */
        public @NotNull Builder<T> type(@NotNull DamageTriggerType type) {
            this.type = type;
            return this;
        }

        /**
         * Sets event mapper
         * @param mapper mapper
         * @return self
         */
        public @NotNull Builder<T> mapper(@NotNull Function<T, DamageEffectData> mapper) {
            this.mapper = mapper;
            return this;
        }

        /**
         * Builds trigger
         * @return trigger
         */
        public @NotNull DamageTrigger<T> build() {
            Objects.requireNonNull(type, "type not set.");
            Objects.requireNonNull(mapper, "mapper not set.");
            return new DamageTrigger<>() {
                @Override
                public @NotNull DamageTriggerType type() {
                    return type;
                }

                @Override
                public @NotNull Class<T> eventClass() {
                    return eventClass;
                }

                @Override
                public @NotNull Function<T, DamageEffectData> mapper() {
                    return mapper;
                }
            };
        }
    }
}
