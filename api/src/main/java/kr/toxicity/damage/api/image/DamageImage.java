package kr.toxicity.damage.api.image;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

/**
 * Image resource of damage skin
 */
public interface DamageImage {

    /**
     * A codepoint string of space
     */
    String SPACE_CODEPOINT = "" + Character.highSurrogate(0xD0000) + Character.lowSurrogate(0xD0000);

    /**
     * Gets a resource key of this image
     * @return key
     */
    @NotNull Key key();
}
