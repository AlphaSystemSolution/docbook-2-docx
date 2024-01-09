package com.alphasystem.docbook.builder.test;

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

    public Setup() {
        super("");
    }

    @BeforeSuite
    public void setup() {
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
