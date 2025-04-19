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
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.Math.*;

public interface DamageEffect {
    @NotNull DamageImage image();
    int duration();
    int interval();
    double showPlayerInRadius();
    @Nullable TextColor color();
    @NotNull TEquation damageModifier();
    @NotNull TransformationEquation transformation();
    @NotNull Display.Billboard billboard();
    @NotNull TEquation blockLight();
    @NotNull TEquation skyLight();
    @NotNull TEquation opacity();
    @NotNull DamageEffectCounter counter();
    @NotNull NumberFormat numberFormat();

    default @NotNull Stream<Player> playerInRadius(@NotNull Player player, @NotNull Location location) {
        var r = showPlayerInRadius();
        return r <= 0.0 ? Stream.empty() : location.getWorld().getNearbyEntities(location, r, r, r)
                .stream()
                .map(e -> e instanceof Player other ? other : null)
                .filter(p -> p != player)
                .filter(Objects::nonNull)
                .filter(OfflinePlayer::isOnline);
    }

    default @NotNull String toString(double damage) {
        var sb = new StringBuilder();
        var index = 0;
        var array = numberFormat().format(damageModifier().evaluate(damage))
                .codePoints()
                .toArray();
        for (int codepoint : array) {
            sb.appendCodePoint(codepoint);
            if (++index < array.length) sb.append(DamageImage.SPACE_CODEPOINT);
        }
        return sb.toString();
    }

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
        display.text(
                Component.text()
                        .content(toString(data.damage()))
                        .color(color())
                        .font(image().key())
                        .build()
        );
        display.spawn(data.player());
        playerInRadius(data.player(), initialLocation).forEach(display::spawn);
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
