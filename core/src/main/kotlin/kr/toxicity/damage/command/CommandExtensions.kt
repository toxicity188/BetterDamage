package kr.toxicity.damage.command

import kr.toxicity.damage.api.effect.DamageEffect
import kr.toxicity.damage.api.skin.DamageSkin
import kr.toxicity.damage.manager.DamageSkinManagerImpl
import kr.toxicity.damage.manager.EffectManagerImpl
import org.bukkit.command.CommandSender
import org.incendo.cloud.CommandManager
import org.incendo.cloud.context.CommandContext

fun CommandManager<CommandSender>.register(
    name: String,
    description: String,
    vararg aliases: String,
    block: CommandBuilder.() -> Unit
) = CommandBuildContext(this, name, description, *aliases).run {
    root.block()
    build()
}


inline fun CommandContext<*>.effect(key: String, notFound: (String) -> DamageEffect) = optional<String>(key).map {
    EffectManagerImpl.effect(it)
}.orElse(null) ?: notFound(key)

inline fun CommandContext<*>.skin(key: String, notFound: (String) -> DamageSkin) = optional<String>(key).map {
    DamageSkinManagerImpl.skin(it)
}.orElse(null) ?: notFound(key)

inline fun <T> CommandContext<*>.string(key: String, mapper: (String) -> T) = mapper(get(key))

fun <T> CommandContext<*>.nullableString(key: String, mapper: (String) -> T): T? = optional<String>(key).map { mapper(it) }.orElse(null)

inline fun <reified T : Any> CommandContext<*>.nullable(key: String): T? = optional<T>(key).orElse(null)
inline fun <reified T : Any> CommandContext<*>.nullable(key: String, ifNotFound: T): T = optional<T>(key).orElse(null) ?: ifNotFound
inline fun <reified T : Any> CommandContext<*>.nullable(key: String, ifNotFound: () -> T): T = optional<T>(key).orElse(null) ?: ifNotFound()
