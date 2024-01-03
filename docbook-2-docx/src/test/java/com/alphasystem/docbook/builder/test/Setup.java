package com.alphasystem.docbook.builder.test;

import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

/**
 * @author sali
 */
public class Setup extends AbstractTest2 {

    @BeforeSuite
    public void setup() {
        try {
            final var wmlPackage = WmlPackageBuilder.createPackage(configurationUtils.getTemplate())
                    .styles(configurationUtils.getStyles());
            wordprocessingMLPackage = wmlPackage.getPackage();
        } catch (Docx4JException ex) {
            fail(ex.getMessage(), ex);
        }

        final Path path = get(targetPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                fail(e.getMessage(), e);
            }
        }
    }
}
