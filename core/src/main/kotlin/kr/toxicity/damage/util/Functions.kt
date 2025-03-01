package kr.toxicity.damage.util

import java.io.File
import java.text.DecimalFormat

val COMMA_FORMAT = DecimalFormat("#,###")

fun <T> T?.ifNull(lazyMessage: () -> String): T & Any = this ?: throw RuntimeException(lazyMessage())

fun Number.withComma(): String = COMMA_FORMAT.format(this)

fun String.toFilePath() = replace('/', File.separatorChar)