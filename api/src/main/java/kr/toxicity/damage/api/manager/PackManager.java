package kr.toxicity.damage.api.manager;

import kr.toxicity.damage.api.pack.PackGenerator;
import kr.toxicity.damage.api.pack.PackPath;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface PackManager extends DamageManager {
    @NotNull PackGenerator currentGenerator();
    void addPackGenerator(@NotNull String name, @NotNull PackGenerator generator);
    void pack(@NotNull Map<PackPath, byte[]> byteMap);
}
