package kr.toxicity.damage.api.image;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface DamageImage {

    String SPACE_CODEPOINT = "" + Character.highSurrogate(0xD0000) + Character.lowSurrogate(0xD0000);

    @NotNull Key key();
}
