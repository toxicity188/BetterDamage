package kr.toxicity.damage.api.pack;

import org.jetbrains.annotations.NotNull;

/**
 * Pack path
 */
public record PackPath(String path) implements Comparable<PackPath> {

    /**
     * Delimiter
     */
    public static final String DELIMITER = "/";

    /**
     * Assets
     */
    public static final PackPath ASSETS = new PackPath("assets");

    /**
     * Creates a pack path
     *
     * @param path path
     */
    public PackPath(@NotNull String path) {
        this.path = path;
    }

    /**
     * Resolves this path
     *
     * @param paths sub paths
     * @return resolved path
     */
    public @NotNull PackPath resolve(@NotNull String... paths) {
        var joined = String.join(DELIMITER, paths);
        return new PackPath(path.isEmpty() ? joined : path + DELIMITER + joined);
    }

    /**
     * Joins this path as zip entry
     *
     * @return zip entry
     */
    @Override
    public @NotNull String path() {
        return path;
    }

    @Override
    public @NotNull String toString() {
        return path;
    }

    @Override
    public int compareTo(@NotNull PackPath o) {
        return path.compareTo(o.path);
    }
}
