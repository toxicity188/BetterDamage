package kr.toxicity.damage.api.pack;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Pack builder
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
public final class PackBuilder {
    private final Map<PackPath, PackSupplier> builderMap;
    private final PackPath path;

    /**
     * Adds some data to this builder
     * @param fileName file name
     * @param supplier data supplier
     */
    public void add(@NotNull String fileName, @NotNull PackSupplier supplier) {
        var resolve = path.resolve(fileName);
        if (builderMap.put(resolve, supplier) != null) throw new RuntimeException("Name duplication found: " + resolve);
    }

    /**
     * Resolves this builder
     * @param paths paths
     * @return resolved builder
     */
    public @NotNull PackBuilder resolve(@NotNull String... paths) {
        return new PackBuilder(builderMap, path.resolve(paths));
    }
}
