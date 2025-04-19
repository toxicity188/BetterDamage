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

/**
 * Pack supplier
 */
@FunctionalInterface
public interface PackSupplier extends Supplier<byte[]> {

    /**
     * Default gson
     */
    Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    /**
     * Gets a supplier of some image
     * @param image image
     * @return supplier
     */
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

    /**
     * Gets a supplier of some byte array
     * @param bytes byte array
     * @return supplier
     */
    static @NotNull PackSupplier of(byte[] bytes) {
        return () -> bytes;
    }
    /**
     * Gets a supplier of some JSON element
     * @param element JSON element
     * @return supplier
     */
    static @NotNull PackSupplier of(@NotNull JsonElement element) {
        return () -> GSON.toJson(element).getBytes(StandardCharsets.UTF_8);
    }
}
