package com.alphasystem.docbook.builder.test;

import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import java.awt.*;
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
    final var file = get(targetPath, FILE_NAME).toFile();
    try {
      WmlAdapter.save(file, ApplicationController.getContext().getWordprocessingMLPackage());
    } catch (Exception e) {
      fail(e.getMessage(), e);
    } finally {
      ApplicationController.endContext();
      try {
        Desktop.getDesktop().open(file);
      } catch (Exception e) {
        // Ignore
      }
    }
  }
}
