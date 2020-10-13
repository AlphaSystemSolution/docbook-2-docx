package com.alphasystem.docbook;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Paths.get;

/**
 * @author sali
 */
public class DocBookTest {

    public static void main(String[] args) {

        String path = "/Users/sfali/development/Lightbend/docs/ClusterFundamentals/course.adoc";

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = new Options();
        options.setBackend("docbook");
        asciidoctor.convertFile(Paths.get(path).toFile(), options);

        try {
            final Path destPath = DocumentBuilder.buildDocument(get(path));
            Desktop.getDesktop().open(destPath.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
