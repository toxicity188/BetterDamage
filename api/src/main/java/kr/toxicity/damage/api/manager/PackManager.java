package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.pack.PackGenerator;
import kr.toxicity.damage.api.pack.PackPath;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Pack manager
 */
public interface PackManager extends DamageManager {
    /**
     * Gets the current generator of pack
     * @return generator
     */
    @NotNull PackGenerator currentGenerator();

    /**
     * Adds some pack generator to this registry
     * @param name name
     * @param generator generator
     */
    void addPackGenerator(@NotNull String name, @NotNull PackGenerator generator);

    /**
     * Packs some byte array map to default location
     * @param byteMap byte array map
     */
    void pack(@NotNull Map<PackPath, byte[]> byteMap);
}
