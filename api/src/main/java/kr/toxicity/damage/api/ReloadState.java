package kr.toxicity.damage.api;

import kr.toxicity.damage.api.pack.PackPath;
import org.jetbrains.annotations.NotNull;

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
     * @param time reload time
     * @param byteMap packed resource pack [resource location: byte array file]
     */
    record Success(long time, @NotNull Map<PackPath, byte[]> byteMap) implements ReloadState {}

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
