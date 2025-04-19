package kr.toxicity.damage.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * File util
 */
public final class FileUtil {
    /**
     * No initializer
     */
    private FileUtil() {
        throw new RuntimeException();
    }

    /**
     * Deletes all files in this folder or file.
     * @param file file
     * @return success
     */
    public static boolean deleteAll(@NotNull File file) {
        if (file.isDirectory()) {
            var list = file.listFiles();
            if (list != null) for (File sub : list) {
                deleteAll(sub);
            }
        }
        return file.delete();
    }
}
