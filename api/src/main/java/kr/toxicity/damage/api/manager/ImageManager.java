package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.image.DamageImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ImageManager extends DamageManager {
    @Nullable DamageImage image(@NotNull String name);
}
