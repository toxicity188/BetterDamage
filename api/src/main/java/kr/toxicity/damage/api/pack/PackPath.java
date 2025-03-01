package kr.toxicity.damage.api.pack;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PackPath {

    public static final PackPath ASSETS = new PackPath("assets");

    private final List<String> pathList;

    private PackPath(@NotNull List<String> pathList) {
        this.pathList = pathList;
    }

    public PackPath(@NotNull String... paths) {
        this(List.of(paths));
    }

    public @NotNull PackPath resolve(@NotNull String... subPaths) {
        var list = new ArrayList<String>(pathList.size() + subPaths.length);
        list.addAll(pathList);
        list.addAll(List.of(subPaths));
        return new PackPath(list);
    }

    public @NotNull String join(@NotNull CharSequence delimiter) {
        return String.join(delimiter, pathList);
    }

    public @NotNull String entry() {
        return join("/");
    }

    @Override
    public String toString() {
        return join(File.separator);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PackPath packPath)) return false;
        return Objects.equals(pathList, packPath.pathList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pathList);
    }
}
