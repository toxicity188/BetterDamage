package kr.toxicity.damage.api;

import kr.toxicity.damage.api.pack.PackPath;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public sealed interface ReloadState {
    OnReload ON_RELOAD = new OnReload();

    record Success(long time, @NotNull Map<PackPath, byte[]> byteMap) implements ReloadState {}
    record Failure(@NotNull Throwable throwable) implements ReloadState {}

    final class OnReload implements ReloadState {
        private OnReload() {
        }
    }
}
