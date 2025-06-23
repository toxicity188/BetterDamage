package kr.toxicity.damage.api.util;

import com.google.gson.JsonParser;
import com.vdurmont.semver4j.Semver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Http util
 */
public final class HttpUtil {

    /**
     * Spigot website
     */
    public static final String VERSION = "https://www.spigotmc.org/resources/123850/";
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .build();

    private static String userId = "%%__USER__%%";
    private static String downloadId = "%%__NONCE__%%";

    /**
     * No initializer
     */
    private HttpUtil() {
        throw new RuntimeException();
    }

    /**
     * Gets latest semver
     * @return semver
     */
    public static @NotNull CompletableFuture<Semver> latest() {
        return CLIENT.sendAsync(HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.spigotmc.org/legacy/update.php?resource=123850/"))
                .build(), HttpResponse.BodyHandlers.ofString()).thenApply(response -> new Semver(response.body(), Semver.SemverType.LOOSE));
    }

    /**
     * Gets user's info future.
     * @return user info
     */
    public static @NotNull CompletableFuture<UserInfo> userInfo() {
        try {
            return CLIENT.sendAsync(HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("https://api.spigotmc.org/simple/0.2/index.php?action=getAuthor&id=" + userId))
                            .build(), HttpResponse.BodyHandlers.ofInputStream())
                    .thenApply(i -> {
                        try (var reader = new InputStreamReader(i.body(), StandardCharsets.UTF_8)) {
                            var name = JsonParser.parseReader(reader)
                                    .getAsJsonObject()
                                    .get("username");
                            return name == null ? new UserInfo(null, null) : new UserInfo(name.getAsString(), downloadId);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new UserInfo(null, null));
        }
    }

    /**
     * Parses semver as component.
     * @param semver semver
     * @return component
     */
    public static @NotNull Component versionComponent(@NotNull Semver semver) {
        return Component.text()
                .content(semver.getOriginalValue())
                .color(NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(
                        Component.text()
                                .append(Component.text(VERSION).color(NamedTextColor.DARK_AQUA))
                                .appendNewline()
                                .append(Component.text("Click to open download link."))
                ))
                .clickEvent(ClickEvent.openUrl(VERSION))
                .build();
    }

    /**
     * User's info
     * @param userName user name
     * @param downloadNumber download number
     */
    public record UserInfo(@Nullable String userName, @Nullable String downloadNumber) {
        /**
         * Gets a log message component by given user info.
         * @return message string
         */
        public @NotNull String toLogMessage() {
            if (userName != null && downloadNumber != null) {
                return "Spigot licensed to: " + userName + " (" + downloadNumber + ")";
            } else {
                return "Unknown license.";
            }
        }
    }
}
