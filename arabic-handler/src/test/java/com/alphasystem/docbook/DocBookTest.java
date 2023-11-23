package com.alphasystem.docbook;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author sali
 */
public class DocBookTest {

    /*
        VM args: --add-opens java.base/sun.nio.ch=ALL-UNNAMED
            --add-opens java.base/java.io=ALL-UNNAMED
            -Dstyles=conf/styles.xml
            -Dtemplate=conf/default.dotx -Dconf.path=.
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("File path not provided");
            System.exit(-1);
            return;
        }

        Path docxPath = null;
        if (args.length == 2) {
            docxPath = Paths.get(args[1]);
        }

        try  {
            Path srcPath = Paths.get(args[0]);
            Path destPath;
            if (docxPath == null) {
                destPath = DocumentBuilder.buildDocument(srcPath);
            } else {
                destPath = DocumentBuilder.buildDocument(srcPath, docxPath);
            }
            Desktop.getDesktop().open(destPath.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
