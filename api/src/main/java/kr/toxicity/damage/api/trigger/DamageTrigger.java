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

public interface DamageTrigger<T extends Event> {

    Listener LISTENER = new Listener() {
    };

    static <C extends Event> @NotNull Builder<C> builder(@NotNull Class<C> eventClass) {
        return new Builder<>(eventClass);
    }

    @NotNull DamageTriggerType type();
    @NotNull Class<T> eventClass();
    @NotNull Function<T, DamageEffectData> mapper();

    default void register() {
        Bukkit.getPluginManager().registerEvent(
                eventClass(),
                LISTENER,
                EventPriority.MONITOR,
                (listener, event) -> {
                    if (!eventClass().isInstance(event)) return;
                    var data = mapper().apply(eventClass().cast(event));
                    if (data == null) return;
                    Optional.ofNullable(BetterDamage.inst().playerManager().player(data.player().getUniqueId()))
                            .map(d -> d.data().effect(type()))
                            .ifPresent(effect -> effect.play(data));
                },
                (JavaPlugin) BetterDamage.inst(),
                true
        );
    }

    @RequiredArgsConstructor
    class Builder<T extends Event> {
        private final Class<T> eventClass;
        private DamageTriggerType type;
        private Function<T, DamageEffectData> mapper;

        public @NotNull Builder<T> type(@NotNull DamageTriggerType type) {
            this.type = type;
            return this;
        }
        public @NotNull Builder<T> mapper(@NotNull Function<T, DamageEffectData> mapper) {
            this.mapper = mapper;
            return this;
        }

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
