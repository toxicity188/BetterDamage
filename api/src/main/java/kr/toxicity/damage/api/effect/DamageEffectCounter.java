package kr.toxicity.damage.api.effect;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class DamageEffectCounter {
    private final Map<UUID, AtomicInteger> countMap = new ConcurrentHashMap<>();

    public int countOf(@NotNull UUID entity) {
        return countMap.computeIfAbsent(entity, u -> new AtomicInteger()).incrementAndGet();
    }

    public void remove(@NotNull UUID entity) {
        var get = countMap.get(entity);
        if (get.decrementAndGet() <= 0) countMap.remove(entity);
    }
}
