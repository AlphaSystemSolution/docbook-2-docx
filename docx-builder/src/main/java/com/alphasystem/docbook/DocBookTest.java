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

    public static void main(String[] args) {

        String path = "/Users/sfali/Documents/Arabic/GrammarLessons/Noun/noun.adoc";

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder().backend("docbook").build();
        asciidoctor.convertFile(Paths.get(path).toFile(), options);

        try {
            final Path destPath = DocumentBuilder.buildDocument(Paths.get(path));
            Desktop.getDesktop().open(destPath.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            asciidoctor.close();
        }
    }

}
