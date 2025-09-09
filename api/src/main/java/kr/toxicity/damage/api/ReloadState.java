package kr.toxicity.damage.api;

import kr.toxicity.damage.api.pack.PackPath;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

/**
 * Reload state
 */
public sealed interface ReloadState {
    /**
     * Singleton instance of on reload
     */
    OnReload ON_RELOAD = new OnReload();

    /**
     * Success
     * @param byteMap packed resource pack [resource location: byte array file]
     * @param directory directory
     * @param time reload time
     */
    record Success(@NotNull Map<PackPath, byte[]> byteMap, @NotNull File directory, long time) implements ReloadState {}

    /**
     * Failure
     * @param throwable reason
     */
    record Failure(@NotNull Throwable throwable) implements ReloadState {}

    /**
     * Still on reload
     */
    final class OnReload implements ReloadState {
        /**
         * Private initializer for singleton instance
         */
        private OnReload() {
        }
    }
}
