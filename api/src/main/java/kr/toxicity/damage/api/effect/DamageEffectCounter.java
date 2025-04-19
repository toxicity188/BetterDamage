package kr.toxicity.damage.api.effect;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Damage effect counter per entity
 */
public final class DamageEffectCounter {
    private final Map<UUID, AtomicInteger> countMap = new ConcurrentHashMap<>();

    /**
     * Gets effect count of entity
     * @param entity entity's uuid
     * @return count
     */
    public int countOf(@NotNull UUID entity) {
        return countMap.computeIfAbsent(entity, u -> new AtomicInteger()).incrementAndGet();
    }

    /**
     * Removes entity's uuid from counter
     * @param entity entity's uuid
     */
    public void remove(@NotNull UUID entity) {
        var get = countMap.get(entity);
        if (get != null && get.decrementAndGet() <= 0) countMap.remove(entity);
    }
}
