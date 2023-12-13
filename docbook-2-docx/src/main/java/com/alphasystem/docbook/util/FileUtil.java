package com.alphasystem.docbook.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAttributes;
import static java.nio.file.Paths.get;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * @author sali
 */
public final class FileUtil {

    private static Path getFile(Path src, String destExtension) {
        final Path parent = src.getParent();
        final Path filePath = src.getFileName();
        final String fileName = filePath.toString();
        final String baseName = getBaseName(fileName);
        return get(parent.toString(), format("%s.%s", baseName, destExtension));
    }

    public static Path getDocBookFile(Path src) {
        return getFile(src, "xml");
    }

    public static Path getDocxFile(Path src) {
        return getFile(src, "docx");
    }

    public static boolean checkFileModified(Path srcPath, Path targetPath) {
        boolean modified = !exists(targetPath);
        if (!modified) {
            try {
                BasicFileAttributes bfa = readAttributes(srcPath, BasicFileAttributes.class);
                FileTime ft = bfa.lastModifiedTime();
                long srcLastModifiedTime = (ft == null) ? 0L : ft.toMillis();
                bfa = readAttributes(targetPath, BasicFileAttributes.class);
                ft = bfa.lastModifiedTime();
                long targetLastModifiedTime = (ft == null) ? 0L : ft.toMillis();
                modified = srcLastModifiedTime > targetLastModifiedTime;
            } catch (IOException e) {
                err.println(e.getMessage());
                modified = true;
            }
        }
        return modified;
    }
}
