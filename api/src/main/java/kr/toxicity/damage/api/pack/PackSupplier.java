package kr.toxicity.damage.api.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@FunctionalInterface
public interface PackSupplier extends Supplier<byte[]> {

    Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    static @NotNull PackSupplier of(@NotNull BufferedImage image) {
        return () -> {
            try (
                    var stream = new ByteArrayOutputStream(1024)
            ) {
                ImageIO.write(image, "png", stream);
                return stream.toByteArray();
            } catch (@NotNull IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static @NotNull PackSupplier of(byte[] bytes) {
        return () -> bytes;
    }

    static @NotNull PackSupplier of(@NotNull JsonElement element) {
        return () -> {
            var builder = new StringBuilder();
            GSON.toJson(element, builder);
            return builder.toString().getBytes(StandardCharsets.UTF_8);
        };
    }
}
