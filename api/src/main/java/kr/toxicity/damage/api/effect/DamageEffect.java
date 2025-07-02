package kr.toxicity.damage.api.effect;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.data.DamageEffectData;
import kr.toxicity.damage.api.equation.EquationData;
import kr.toxicity.damage.api.equation.TEquation;
import kr.toxicity.damage.api.equation.TransformationEquation;
import kr.toxicity.damage.api.event.CreateDamageEffectEvent;
import kr.toxicity.damage.api.image.DamageImage;
import kr.toxicity.damage.api.scheduler.DamageScheduler;
import kr.toxicity.damage.api.util.BitUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.text.NumberFormat;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.Math.*;

/**
 * Damage effect
 */
public interface DamageEffect {
    /**
     * Gets image resource
     * @return image
     */
    @NotNull DamageImage image();

    /**
     * Gets running duration of this effect
     * @return duration
     */
    int duration();

    /**
     * Gets an interval of this effect
     * @return interval
     */
    int interval();

    /**
     * Checks this damage effect should be rendered by all tracked player
     * @return show global player
     */
    boolean showGlobalPlayer();

    /**
     * Gets a text color of this effect
     * @return color
     */
    @Nullable TextColor color();

    /**
     * Gets a damage modifier of this effect
     * @return modifier
     */
    @NotNull TEquation damageModifier();

    /**
     * Gets a transformation equation of this effect
     * @return transformation equation
     */
    @NotNull TransformationEquation transformation();

    /**
     * Gets a billboard of this effect
     * @return billboard
     */
    @NotNull Display.Billboard billboard();

    /**
     * Gets a block light-level of this effect
     * @return block light-level
     */
    @NotNull TEquation blockLight();

    /**
     * Gets a sky light-level of this effect
     * @return sky light-level
     */
    @NotNull TEquation skyLight();

    /**
     * Gets opacity of this effect
     * @return opacity
     */
    @NotNull TEquation opacity();

    /**
     * Gets a counter of this effect
     * @return counter
     */
    @NotNull DamageEffectCounter counter();

    /**
     * Gets a number format of this effect
     * @return format
     */
    @NotNull NumberFormat numberFormat();

    /**
     * Gets a shadow color of this effect
     * @return shadow color
     */
    int shadowColor();

    /**
     * Parses this damage to string
     * @param damage damage
     * @return string value
     */
    default @NotNull String toString(double damage) {
        var sb = new StringBuilder();
        var index = 0;
        var space = image().space();
        var damageString = numberFormat().format(damageModifier().evaluate(damage));
        if (space == 0) return damageString;
        var array = damageString
                .codePoints()
                .toArray();
        for (int codepoint : array) {
            sb.appendCodePoint(codepoint);
            if (++index < array.length) sb.append(DamageImage.SPACE_CODEPOINT);
        }
        return sb.toString();
    }

    /**
     * Plays damage effect
     * @param data data
     * @return effect task
     */
    default @NotNull DamageScheduler.ScheduledTask play(@NotNull DamageEffectData data) {
        var event = new CreateDamageEffectEvent(this, data);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return DamageScheduler.EMPTY;
        var index = new AtomicInteger();
        var count = counter().countOf(data.entity().getUniqueId());
        var equationData = new EquationData(interval(), count);
        var equation = transformation().reader(equationData);
        var initialLocation = data.entity().getLocation().add(
                0,
                Optional.ofNullable(BetterDamage.inst().modelAdapter())
                        .map(a -> a.height(data.entity()))
                        .orElse(data.entity().getHeight()),
                0
        );
        var display = BetterDamage.inst().nms().create(initialLocation);
        display.frame(interval() + 1);
        display.billboard(billboard());
        var builder = Component.text()
                .content(toString(data.damage()))
                .color(color())
                .font(image().key());
        if (BetterDamage.inst().version().supportShadowColor()) builder.shadowColor(ShadowColor.shadowColor(shadowColor()));
        display.text(builder.build());
        var stream = Stream.of(data.player());
        if (showGlobalPlayer()) stream = Stream.concat(stream, data.entity().getTrackedBy().stream());
        stream.forEach(display::spawn);
        var limit = duration() / interval();
        var blockLight = blockLight().reader(equationData);
        var skyLight = skyLight().reader(equationData);
        var opacity = opacity().reader(equationData);
        return BetterDamage.inst().scheduler().async(0, interval(), task -> {
            var get = index.getAndIncrement();
            if (get < limit) {
                var next = equation.next();
                var rotated = new Vector3f(next.getTranslation())
                        .rotate(new Quaternionf()
                                .rotateZYX(0, (float) -toRadians(initialLocation.getYaw()), 0)
                                .mul(next.getLeftRotation())
                        );
                var teleport = initialLocation.clone().add(
                        rotated.x(),
                        rotated.y(),
                        rotated.z()
                );
                display.brightness(new Display.Brightness(
                        round(clamp(blockLight.next(), 0F, 15F)),
                        round(clamp(skyLight.next(), 0F, 15F))
                ));
                display.teleport(teleport);
                display.opacity(BitUtil.opacityToByte(opacity.next()));
                display.transformation(new Transformation(new Vector3f(), new Quaternionf(), next.getScale(), new Quaternionf()));
                display.update();
            } else {
                display.remove();
                counter().remove(data.entity().getUniqueId());
                task.cancel();
            }
        });
    }
}
