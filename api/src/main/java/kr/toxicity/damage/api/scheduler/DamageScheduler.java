package kr.toxicity.damage.api.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * BetterDamage's scheduler.
 */
public interface DamageScheduler {

    ScheduledTask EMPTY = () -> {};

    @NotNull ScheduledTask async(@NotNull Consumer<ScheduledTask> task);
    @NotNull ScheduledTask async(long delay, long period, @NotNull Consumer<ScheduledTask> task);

    @FunctionalInterface
    interface ScheduledTask {
        void cancel();
    }
}
