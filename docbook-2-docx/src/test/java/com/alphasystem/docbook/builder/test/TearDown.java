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
public class TearDown extends AbstractTest2 {


    @AfterSuite
    public void tearDown() {
        ApplicationController.endContext();
        try {
            final File file = get(targetPath, FILE_NAME).toFile();
            WmlAdapter.save(file, wordprocessingMLPackage);
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }
}
