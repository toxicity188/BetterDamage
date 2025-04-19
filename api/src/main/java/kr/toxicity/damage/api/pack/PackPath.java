package kr.toxicity.damage.api.pack;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Pack path
 */
public final class PackPath {

    /**
     * Assets
     */
    public static final PackPath ASSETS = new PackPath("assets");

    private final List<String> pathList;

    private PackPath(@NotNull List<String> pathList) {
        this.pathList = pathList;
    }

    /**
     * Creates a pack path
     * @param paths paths
     */
    public PackPath(@NotNull String... paths) {
        this(List.of(paths));
    }

    /**
     * Resolves this path
     * @param subPaths sub paths
     * @return resolved path
     */
    public @NotNull PackPath resolve(@NotNull String... subPaths) {
        var list = new ArrayList<String>(pathList.size() + subPaths.length);
        list.addAll(pathList);
        list.addAll(List.of(subPaths));
        return new PackPath(list);
    }

    /**
     * Joins this path
     * @param delimiter delimiter
     * @return joined string
     */
    public @NotNull String join(@NotNull CharSequence delimiter) {
        return String.join(delimiter, pathList);
    }

    /**
     * Joins this path as zip entry
     * @return zip entry
     */
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
