package kr.toxicity.damage.api.adapter;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model compatibility adapter
 */
public interface ModelAdapter {
    /**
     * Gets a height of this entity
     * @param entity entity
     * @return height or null
     */
    @Nullable Double height(@NotNull Entity entity);
}
