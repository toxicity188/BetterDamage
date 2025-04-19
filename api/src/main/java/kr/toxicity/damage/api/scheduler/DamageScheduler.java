package kr.toxicity.damage.api.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * BetterDamage's scheduler.
 */
public interface DamageScheduler {

    /**
     * Empty scheduler task
     */
    ScheduledTask EMPTY = () -> {};

    /**
     * Executes async task
     * @param task task
     * @return scheduled task
     */
    @NotNull ScheduledTask async(@NotNull Consumer<ScheduledTask> task);

    /**
     * Executes async task
     * @param delay delay
     * @param period period
     * @param task task
     * @return scheduled task
     */
    @NotNull ScheduledTask async(long delay, long period, @NotNull Consumer<ScheduledTask> task);

    /**
     * Scheduled task
     */
    @FunctionalInterface
    interface ScheduledTask {
        /**
         * Cancel this task
         */
        void cancel();
    }
}
