package kr.toxicity.damage.api.adapter;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModelAdapter {
    @Nullable Double height(@NotNull Entity entity);
}
