package kr.toxicity.damage.effect

import kr.toxicity.damage.api.BetterDamage
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

    private val image = section.getString("image").ifNull { "The value 'image' doesn't exist." }.let {
        ImageManagerImpl.image(it).ifNull { "This image doesn't exist: $it" }
    }
    private val duration = section.getInt("duration", 20).coerceAtLeast(1)
    private val interval = section.getInt("interval", 1).coerceAtLeast(1)
    private val showPlayerInRadius = section.getDouble("show-player-in-radius", 0.0).coerceAtLeast(0.0)
    private val color = section.getString("color")?.toTextColor()
    private val transform = TransformationEquation(section.getConfigurationSection("transformation").ifNull { "The value 'transformation' doesn't exist." })
    private val billboard = section.getString("billboard")?.let {
        runCatching {
            Display.Billboard.valueOf(it.uppercase())
        }.getOrElse { e ->
            throw RuntimeException("This billboard doesn't exist: $it")
        }
    } ?: Display.Billboard.CENTER
    private val damageModifier = section.getAsEquation("damage-modifier") ?: TEquation.T
    private val blockLight = section.getAsEquation("block-light") ?: TEquation.FULL_LIGHT
    private val skyLight = section.getAsEquation("sky-light") ?: TEquation.FULL_LIGHT
    private val opacity = section.getAsEquation("opacity") ?: TEquation.FULL_OPACITY
    private val counter = DamageEffectCounter()
    private val numberFormat = runCatching {
        if (section.isSet("number-format")) {
            DecimalFormat(section.getString("number-format", "#"))
        } else {
            null
        }
    }.getOrNull()

    override fun image(): DamageImage = image
    override fun duration(): Int = duration
    override fun interval(): Int = interval
    override fun showPlayerInRadius(): Double = showPlayerInRadius
    override fun color(): TextColor? = color
    override fun transformation(): TransformationEquation = transform
    override fun billboard(): Display.Billboard = billboard
    override fun damageModifier(): TEquation = damageModifier
    override fun blockLight(): TEquation = blockLight
    override fun skyLight(): TEquation = skyLight
    override fun counter(): DamageEffectCounter = counter
    override fun opacity(): TEquation = opacity
    override fun numberFormat(): NumberFormat = numberFormat ?: if (BetterDamage.inst().configManager().showDecimalDamage()) {
        DecimalFormat("#.#") // Format avec une décimale si l'option est activée
    } else {
        DecimalFormat("#") // Format entier par défaut
    }
}
