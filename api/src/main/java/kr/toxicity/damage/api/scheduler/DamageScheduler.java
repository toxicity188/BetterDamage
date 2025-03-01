package kr.toxicity.damage.api.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * BetterDamage's scheduler.
 */
public interface DamageScheduler {

    @NotNull ScheduledTask async(@NotNull Consumer<ScheduledTask> task);
    @NotNull ScheduledTask async(long delay, long period, @NotNull Consumer<ScheduledTask> task);

    interface ScheduledTask {
        void cancel();
    }
}
