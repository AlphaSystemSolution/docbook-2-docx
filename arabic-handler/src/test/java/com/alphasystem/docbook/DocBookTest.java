package com.alphasystem.docbook;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

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
        if(args == null || args.length == 0) {
            System.err.println("File path not provided");
            System.exit(-1);
            return;
        }

        String path = args[0];

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder().backend("docbook").build();
        Path srcPath = Paths.get(path);
        asciidoctor.convertFile(srcPath.toFile(), options);

        try {
            final Path destPath = DocumentBuilder.buildDocument(srcPath);
            Desktop.getDesktop().open(destPath.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            asciidoctor.close();
        }
    }

}
