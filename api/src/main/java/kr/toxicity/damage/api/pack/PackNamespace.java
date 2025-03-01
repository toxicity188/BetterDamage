package kr.toxicity.damage.api.pack;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class PackNamespace {

    private final PackBuilder font, textures;

    public PackNamespace(@NotNull Map<PackPath, PackSupplier> builderMap, @NotNull PackPath path) {
        font = new PackBuilder(builderMap, path.resolve("font"));
        textures = new PackBuilder(builderMap, path.resolve("textures"));
    }

    public @NotNull PackBuilder font() {
        return font;
    }

    public @NotNull PackBuilder textures() {
        return textures;
    }
}
