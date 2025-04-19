package kr.toxicity.damage.api.pack;

import kr.toxicity.damage.api.BetterDamage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Resource pack assets.
 */
public final class PackAssets {

    private final Map<PackPath, PackSupplier> builderMap;
    private final PackNamespace minecraft, betterDamage;

    public PackAssets() {
        builderMap = new HashMap<>();
        minecraft = new PackNamespace(builderMap, PackPath.ASSETS.resolve("minecraft"));
        betterDamage = new PackNamespace(builderMap, PackPath.ASSETS.resolve(BetterDamage.inst().configManager().namespace()));
    }

    /**
     * Gets minecraft namespace.
     * @return minecraft
     */
    public @NotNull PackNamespace minecraft() {
        return minecraft;
    }

    /**
     * Gets BetterDamage namespace.
     * @return BetterDamage
     */
    public @NotNull PackNamespace betterDamage() {
        return betterDamage;
    }

    /**
     * Adds some data to assets
     * @param filePath path
     * @param supplier data supplier
     */
    public void add(@NotNull PackPath filePath, @NotNull PackSupplier supplier) {
        if (builderMap.put(filePath, supplier) != null) throw new RuntimeException("Name duplication found: " + filePath);
    }

    /**
     * Builds all data as a byte array.
     * @return byte array map
     */
    public @NotNull Map<PackPath, byte[]> build() {
        return builderMap.entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
