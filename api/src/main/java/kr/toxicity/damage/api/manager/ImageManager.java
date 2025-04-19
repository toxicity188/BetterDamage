package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.image.DamageImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Image manager
 */
public interface ImageManager extends DamageManager {
    /**
     * Gets some image by name
     * @param name name
     * @return image or null
     */
    @Nullable DamageImage image(@NotNull String name);
}
