package kr.toxicity.damage.api.image;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

/**
 * Image resource of damage skin
 */
public interface DamageImage {
    /**
     * Not set
     */
    DamageImage NOT_SET = new DamageImage() {

        private final Key defaultKey = Key.key("default");

        @Override
        public int space() {
            return 0;
        }

        @Override
        public @NotNull Key key() {
            return defaultKey;
        }
    };

    /**
     * A codepoint string of space
     */
    String SPACE_CODEPOINT = "" + Character.highSurrogate(0xD0000) + Character.lowSurrogate(0xD0000);

    /**
     * Gets a space width of this image
     * @return space
     */
    int space();

    /**
     * Gets a resource key of this image
     * @return key
     */
    @NotNull Key key();
}
