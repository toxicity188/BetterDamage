package kr.toxicity.damage.effect

import kr.toxicity.damage.api.effect.DamageEffect
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

class DamageEffectImpl(section: ConfigurationSection) : DamageEffect {

    private val image = section.getString("image").ifNull { "The value 'image' doesn't exist." }.let {
        ImageManagerImpl.image(it).ifNull { "This image doesn't exist: $it" }
    }
    private val duration = section.getInt("duration", 20).coerceAtLeast(1)
    private val interval = section.getInt("interval", 1).coerceAtLeast(1)
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

    override fun image(): DamageImage = image
    override fun duration(): Int = duration
    override fun interval(): Int = interval
    override fun color(): TextColor? = color
    override fun transformation(): TransformationEquation = transform
    override fun billboard(): Display.Billboard = billboard
    override fun damageModifier(): TEquation = damageModifier
    override fun blockLight(): TEquation = blockLight
    override fun skyLight(): TEquation = skyLight
}