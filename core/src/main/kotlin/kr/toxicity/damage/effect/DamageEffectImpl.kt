package kr.toxicity.damage.effect

import kr.toxicity.damage.api.effect.DamageEffect
import kr.toxicity.damage.api.effect.DamageEffectCounter
import kr.toxicity.damage.api.equation.TEquation
import kr.toxicity.damage.api.equation.TransformationEquation
import kr.toxicity.damage.api.image.DamageImage
import kr.toxicity.damage.manager.ImageManagerImpl
import kr.toxicity.damage.util.getAsEquation
import kr.toxicity.damage.util.ifNull
import kr.toxicity.damage.util.toTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Display
import java.text.DecimalFormat
import java.text.NumberFormat

class DamageEffectImpl(section: ConfigurationSection) : DamageEffect {

    private val image = section.getString("image")?.let {
        ImageManagerImpl.image(it).ifNull { "This image doesn't exist: $it" }
    } ?: DamageImage.NOT_SET
    private val duration = section.getInt("duration", 20).coerceAtLeast(1)
    private val interval = section.getInt("interval", 1).coerceAtLeast(1)
    private val showGlobalPlayer = section.getBoolean("show-global-player")
    private val color = section.getString("color")?.toTextColor()
    private val transform = TransformationEquation(section.getConfigurationSection("transformation").ifNull { "The value 'transformation' doesn't exist." })
    private val billboard = section.getString("billboard")?.let {
        runCatching {
            Display.Billboard.valueOf(it.uppercase())
        }.getOrElse { e ->
            throw RuntimeException("This billboard doesn't exist: $it", e)
        }
    } ?: Display.Billboard.CENTER
    private val damageModifier = section.getAsEquation("damage-modifier") ?: TEquation.T
    private val blockLight = section.getAsEquation("block-light") ?: TEquation.FULL_LIGHT
    private val skyLight = section.getAsEquation("sky-light") ?: TEquation.FULL_LIGHT
    private val opacity = section.getAsEquation("opacity") ?: TEquation.FULL_OPACITY
    private val counter = DamageEffectCounter()
    private val numberFormat = runCatching {
        DecimalFormat(section.getString("number-format", "#"))
    }.getOrNull() ?: DecimalFormat("#")
    private val shadowColor = section.getString("shadow-color")?.let {
        runCatching {
            (it.toLong(16) and 0xFFFFFFFF).toInt()
        }.getOrNull()
    } ?: 0

    override fun image(): DamageImage = image
    override fun duration(): Int = duration
    override fun interval(): Int = interval
    override fun showGlobalPlayer(): Boolean = showGlobalPlayer
    override fun color(): TextColor? = color
    override fun transformation(): TransformationEquation = transform
    override fun billboard(): Display.Billboard = billboard
    override fun damageModifier(): TEquation = damageModifier
    override fun blockLight(): TEquation = blockLight
    override fun skyLight(): TEquation = skyLight
    override fun counter(): DamageEffectCounter = counter
    override fun opacity(): TEquation = opacity
    override fun numberFormat(): NumberFormat = numberFormat
    override fun shadowColor(): Int = shadowColor
}