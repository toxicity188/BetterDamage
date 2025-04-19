package kr.toxicity.damage.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

/**
 * Virtual text display
 */
public interface DamageDisplay {
    /**
     * Spawns this display to some player's client
     * @param player player
     */
    void spawn(@NotNull Player player);

    /**
     * Teleports this display
     * @param location location
     */
    void teleport(@NotNull Location location);

    /**
     * Sets the shown text of this display
     * @param component text component
     */
    void text(@NotNull Component component);

    /**
     * Sets the transformation of this display
     * @param transformation transformation
     */
    void transformation(@NotNull Transformation transformation);

    /**
     * Sets the brightness of this display
     * @param brightness brightness
     */
    void brightness(@NotNull Display.Brightness brightness);

    /**
     * Updates and resends all entity data of this display
     */
    void update();

    /**
     * Removes this display from all player's client
     */
    void remove();

    /**
     * Sets display billboard of this display
     * @param billboard billboard
     */
    void billboard(@NotNull Display.Billboard billboard);

    /**
     * Sets the interpolation duration of this display
     * @param frame frame
     */
    void frame(int frame);

    /**
     * Sets the opacity of this display
     * @param opacity opacity
     */
    void opacity(byte opacity);
}
