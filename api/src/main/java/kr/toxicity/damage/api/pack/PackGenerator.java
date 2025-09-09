package kr.toxicity.damage.api.pack;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Pack generator
 */
public interface PackGenerator {

    /**
     * Generates as directory
     */
    PackGenerator FOLDER = new PackGenerator() {
        @Override
        public void build(@NotNull File dir, @NotNull Map<PackPath, byte[]> byteMap) {
            if (dir.exists() && !FileUtil.deleteAll(dir)) BetterDamage.inst().getLogger().warning("Unable to delete " + dir.getPath());
            byteMap.entrySet()
                    .parallelStream()
                    .forEach(entry -> {
                        var path = entry.getKey().path();
                        var file = new File(dir, path);
                        file.getParentFile().mkdirs();
                        try (
                                var stream = new FileOutputStream(file);
                                var buffered = new BufferedOutputStream(stream)
                        ) {
                            buffered.write(entry.getValue());
                        } catch (@NotNull IOException e) {
                            BetterDamage.inst().handle(e, "Unable to write this file: " + path);
                        }
                    });
        }

        @Override
        public @NotNull String extension() {
            return "";
        }
    };

    /**
     * Generates as SHA-1 resource pack.
     */
    PackGenerator ZIP = new PackGenerator() {
        @Override
        public void build(@NotNull File dir, @NotNull Map<PackPath, byte[]> byteMap) {
            try (
                    var file = new FileOutputStream(dir);
                    var buffered = new BufferedOutputStream(file);
                    var digest = new DigestOutputStream(buffered, MessageDigest.getInstance("SHA-1"));
                    var zip = new ZipOutputStream(digest)
            ) {
                zip.setComment("BetterDamage's resource pack");
                zip.setLevel(Deflater.BEST_COMPRESSION);
                for (Map.Entry<PackPath, byte[]> entry : byteMap.entrySet()) {
                    zip.putNextEntry(new ZipEntry(entry.getKey().path()));
                    zip.write(entry.getValue());
                    zip.closeEntry();
                }
            } catch (@NotNull Exception e) {
                BetterDamage.inst().handle(e, "Unable to write zip file");
            }
        }

        @Override
        public @NotNull String extension() {
            return "zip";
        }
    };

    /**
     * Generates all files by byte map
     * @param dir location
     * @param byteMap byte array map
     */
    void build(@NotNull File dir, @NotNull Map<PackPath, byte[]> byteMap);

    @NotNull String extension();
}
