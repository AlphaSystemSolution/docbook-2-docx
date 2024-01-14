package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.testng.annotations.AfterSuite;

import java.awt.*;
import java.io.File;

import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

/**
 * @author sali
 */
public class TearDown extends AbstractTest {

    public TearDown() {
        super("");
    }

    @AfterSuite
    public void tearDown() {
        try {
            final File file = get(targetPath, FILE_NAME).toFile();
            WmlAdapter.save(file, ApplicationController.getContext().getWordprocessingMLPackage());
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        } finally {
            ApplicationController.endContext();
        }
    }
}
