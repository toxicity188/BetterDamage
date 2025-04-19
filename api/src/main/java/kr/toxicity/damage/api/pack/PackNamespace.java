package kr.toxicity.damage.api.pack;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Namespace of assets
 */
public final class PackNamespace {

    private final PackBuilder font, textures;

    PackNamespace(@NotNull Map<PackPath, PackSupplier> builderMap, @NotNull PackPath path) {
        font = new PackBuilder(builderMap, path.resolve("font"));
        textures = new PackBuilder(builderMap, path.resolve("textures"));
    }

    /**
     * Gets assets/namespace/font
     * @return font
     */
    public @NotNull PackBuilder font() {
        return font;
    }

    /**
     * Gets assets/namespace/textures
     * @return textures
     */
    public @NotNull PackBuilder textures() {
        return textures;
    }
}
