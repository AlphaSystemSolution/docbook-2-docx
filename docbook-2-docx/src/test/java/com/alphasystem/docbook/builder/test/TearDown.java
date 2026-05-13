package com.alphasystem.docbook.builder.test;

import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import java.awt.*;
import java.io.File;
import org.testng.annotations.AfterSuite;

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
