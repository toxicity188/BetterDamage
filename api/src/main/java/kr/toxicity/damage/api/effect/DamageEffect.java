package kr.toxicity.damage.api.effect;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.data.DamageEffectData;
import kr.toxicity.damage.api.equation.TEquation;
import kr.toxicity.damage.api.equation.TransformationEquation;
import kr.toxicity.damage.api.image.DamageImage;
import kr.toxicity.damage.api.scheduler.DamageScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.*;

public interface DamageEffect {
    @NotNull DamageImage image();
    int duration();
    int interval();
    @Nullable TextColor color();
    @NotNull TEquation damageModifier();
    @NotNull TransformationEquation transformation();
    @NotNull Display.Billboard billboard();
    @NotNull TEquation blockLight();
    @NotNull TEquation skyLight();

    default @NotNull String toString(double damage) {
        var sb = new StringBuilder();
        var index = 0;
        var array = Long.toString(round(damageModifier().evaluate(damage)))
                .codePoints()
                .toArray();
        for (int codepoint : array) {
            sb.appendCodePoint(codepoint);
            if (++index < array.length) sb.append(DamageImage.SPACE_CODEPOINT);
        }
        return sb.toString();
    }

    default @NotNull DamageScheduler.ScheduledTask play(@NotNull DamageEffectData data) {
        var index = new AtomicInteger();
        var equation = transformation().reader(interval());
        var initialLocation = data.entity().getLocation().add(
                0,
                Optional.ofNullable(BetterDamage.inst().modelAdapter())
                        .map(a -> a.height(data.entity()))
                        .orElse(data.entity().getHeight()),
                0
        );
        var display = BetterDamage.inst().nms().create(data.player(), initialLocation);
        display.frame(interval() + 1);
        display.billboard(billboard());
        display.text(
                Component.text()
                        .content(toString(data.damage()))
                        .color(color())
                        .font(image().key())
                        .build()
        );
        var limit = duration() / interval();
        var blockLight = blockLight().reader(interval());
        var skyLight = skyLight().reader(interval());
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
                display.transformation(new Transformation(new Vector3f(), new Quaternionf(), next.getScale(), new Quaternionf()));
                display.update();
            } else {
                display.remove();
                task.cancel();
            }
        });
    }
}
