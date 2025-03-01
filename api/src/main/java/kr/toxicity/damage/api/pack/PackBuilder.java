package kr.toxicity.damage.api.pack;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
public final class PackBuilder {
    private final Map<PackPath, PackSupplier> builderMap;
    private final PackPath path;

    public void add(@NotNull String fileName, @NotNull PackSupplier supplier) {
        var resolve = path.resolve(fileName);
        if (builderMap.put(resolve, supplier) != null) throw new RuntimeException("Name duplication found: " + resolve);
    }

    public @NotNull PackBuilder resolve(@NotNull String... paths) {
        return new PackBuilder(builderMap, path.resolve(paths));
    }
}
