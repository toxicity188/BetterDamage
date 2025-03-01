package kr.toxicity.damage.scheduler

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.toxicity.damage.api.scheduler.DamageScheduler
import kr.toxicity.damage.util.PLUGIN
import org.bukkit.Bukkit
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class FoliaScheduler : DamageScheduler {
    override fun async(task: Consumer<DamageScheduler.ScheduledTask>): DamageScheduler.ScheduledTask {
        return wrap { t ->
            Bukkit.getAsyncScheduler().runNow(PLUGIN) {
                task.accept(t)
            }
        }
    }

    override fun async(
        delay: Long,
        period: Long,
        task: Consumer<DamageScheduler.ScheduledTask?>
    ): DamageScheduler.ScheduledTask {
        return wrap { t ->
            Bukkit.getAsyncScheduler().runAtFixedRate(PLUGIN, {
                task.accept(t)
            }, (delay * 50).coerceAtLeast(1), (period * 50).coerceAtLeast(1), TimeUnit.MILLISECONDS)
        }
    }

    private fun wrap(creator: (DelegatedTask) -> ScheduledTask) = DelegatedTask(creator)

    private class DelegatedTask(
        creator: (DelegatedTask) -> ScheduledTask
    ) : DamageScheduler.ScheduledTask {
        private val delegate = creator(this)
        override fun cancel() {
            delegate.cancel()
        }
    }
}