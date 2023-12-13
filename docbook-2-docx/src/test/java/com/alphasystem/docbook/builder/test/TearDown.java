package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.ApplicationController;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.testng.annotations.AfterSuite;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.save;
import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

/**
 * @author sali
 */
public class TearDown extends AbstractTest {

    @AfterSuite
    public void tearDown() {
        ApplicationController.endContext();
        try {
            final File file = get(TARGET_PATH, "builder.docx").toFile();
            save(file, wmlPackage);
            Desktop.getDesktop().open(file);
        } catch (Docx4JException e) {
            fail(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
