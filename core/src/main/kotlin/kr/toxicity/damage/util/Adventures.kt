package kr.toxicity.damage.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

fun createAdventureKey(path: String) = Key.key(NAMESPACE, path)

fun String.toTextColor() = if (startsWith('#') && length == 7) TextColor.fromHexString(this) else NamedTextColor.NAMES.value(lowercase()).ifNull {
    "This color doesn't exist: $this"
}

fun <T : Any> (() -> T?).memoizeNullable(): () -> T? = this as? MemoizedSupplier ?: MemoizedSupplier(this)

private class MemoizedSupplier<T : Any>(
    private val delegate: () -> T?
) : () -> T? {

    private var cache: T? = null

    @Synchronized
    override fun invoke(): T? {
        if (cache == null) cache = delegate()
        return cache
    }
}