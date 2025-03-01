package kr.toxicity.damage.api.pack;

import kr.toxicity.damage.api.BetterDamage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class PackAssets {

    private final Map<PackPath, PackSupplier> builderMap;
    private final PackNamespace minecraft, betterDamage;

    public PackAssets() {
        builderMap = new HashMap<>();
        minecraft = new PackNamespace(builderMap, PackPath.ASSETS.resolve("minecraft"));
        betterDamage = new PackNamespace(builderMap, PackPath.ASSETS.resolve(BetterDamage.inst().configManager().namespace()));
    }

    public @NotNull PackNamespace minecraft() {
        return minecraft;
    }

    public @NotNull PackNamespace betterDamage() {
        return betterDamage;
    }

    public void add(@NotNull PackPath filePath, @NotNull PackSupplier supplier) {
        if (builderMap.put(filePath, supplier) != null) throw new RuntimeException("Name duplication found: " + filePath);
    }

    public @NotNull Map<PackPath, byte[]> build() {
        return builderMap.entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
