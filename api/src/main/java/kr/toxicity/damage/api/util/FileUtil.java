package kr.toxicity.damage.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class FileUtil {
    private FileUtil() {
        throw new RuntimeException();
    }

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
