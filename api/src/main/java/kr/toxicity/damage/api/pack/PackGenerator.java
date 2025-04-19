package kr.toxicity.damage.api.pack;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
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
    PackGenerator FOLDER = (dir, map) -> {
        if (dir.exists() && !FileUtil.deleteAll(dir)) BetterDamage.inst().getLogger().warning("Unable to delete " + dir.getPath());
        map.entrySet()
                .parallelStream()
                .forEach(entry -> {
                    var path = entry.getKey().toString();
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
    };

    /**
     * Generates as SHA-1 resource pack.
     */
    PackGenerator ZIP = (dir, map) -> {
        try (
                var file = new FileOutputStream(new File(dir.getParentFile(), dir.getName() + ".zip"));
                var buffered = new BufferedOutputStream(file);
                var digest = new DigestOutputStream(buffered, MessageDigest.getInstance("SHA-1"));
                var zip = new ZipOutputStream(digest)
        ) {
            zip.setComment("BetterDamage's resource pack");
            zip.setLevel(Deflater.BEST_COMPRESSION);
            for (Map.Entry<PackPath, byte[]> entry : map.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey().entry()));
                zip.write(entry.getValue());
                zip.closeEntry();
            }
        } catch (@NotNull Exception e) {
            BetterDamage.inst().handle(e, "Unable to write zip file");
        }
    };

    /**
     * Generates all files by byte map
     * @param dir location
     * @param byteMap byte array map
     */
    void build(@NotNull File dir, @NotNull Map<PackPath, byte[]> byteMap);
}
