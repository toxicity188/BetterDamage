package kr.toxicity.damage.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

public interface DamageDisplay {
    void teleport(@NotNull Location location);
    void text(@NotNull Component component);
    void transformation(@NotNull Transformation transformation);
    void brightness(@NotNull Display.Brightness brightness);
    void update();
    void remove();
    void billboard(@NotNull Display.Billboard billboard);
    void frame(int frame);
    void opacity(byte opacity);
}
