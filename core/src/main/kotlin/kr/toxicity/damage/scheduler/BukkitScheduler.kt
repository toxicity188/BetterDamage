package kr.toxicity.damage.scheduler

import kr.toxicity.damage.api.scheduler.DamageScheduler
import kr.toxicity.damage.util.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import java.util.function.Consumer

class BukkitScheduler : DamageScheduler {
    override fun async(task: Consumer<DamageScheduler.ScheduledTask>): DamageScheduler.ScheduledTask {
        return wrap { t ->
            Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, Runnable {
                task.accept(t)
            })
        }
    }

    override fun async(
        delay: Long,
        period: Long,
        task: Consumer<DamageScheduler.ScheduledTask?>
    ): DamageScheduler.ScheduledTask {
        return wrap {
            Bukkit.getScheduler().runTaskTimerAsynchronously(PLUGIN, Runnable {
                task.accept(it)
            }, delay, period)
        }
    }

    private fun wrap(creator: (DelegatedTask) -> BukkitTask) = DelegatedTask(creator)

    private class DelegatedTask(
        creator: (DelegatedTask) -> BukkitTask
    ) : DamageScheduler.ScheduledTask {
        private val delegate = creator(this)
        override fun cancel() {
            delegate.cancel()
        }
    }
}