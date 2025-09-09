package kr.toxicity.damage.util

import kr.toxicity.damage.BetterDamagePluginImpl
import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.scheduler.DamageScheduler
import org.bukkit.command.CommandSender

val PLUGIN
    get() = BetterDamage.inst() as BetterDamagePluginImpl

val DATA_FOLDER
    get() = PLUGIN.dataFolder

val CONFIG
    get() = PLUGIN.config()

val NAMESPACE
    get() = CONFIG.namespace()

fun info(vararg message: String) {
    val logger = PLUGIN.logger
    synchronized(logger) {
        message.forEach(logger::info)
    }
}

fun warn(vararg message: String) {
    val logger = PLUGIN.logger
    synchronized(logger) {
        message.forEach(logger::warning)
    }
}

fun Throwable.handle(log: String) {
    handle(log) {
        warn(*it.toTypedArray())
    }
}

fun Throwable.handle(sender: CommandSender, log: String) {
    handle(log) {
        synchronized(sender) {
            it.forEach(sender::sendMessage)
        }
    }
}

fun Throwable.handle(log: String, handler: (List<String>) -> Unit) {
    val list = mutableListOf(
        log,
        "Reason: ${message ?: javaClass.name}"
    )
    if (PLUGIN.config().debug()) {
        list += listOf(
            "Stack trace:",
            stackTraceToString()
        )
    }
    handler(list)
}

fun async(block: DamageScheduler.ScheduledTask.() -> Unit) = PLUGIN.scheduler().async(block)
fun async(delay: Long, period: Long, block: DamageScheduler.ScheduledTask.() -> Unit) = PLUGIN.scheduler().async(delay, period, block)