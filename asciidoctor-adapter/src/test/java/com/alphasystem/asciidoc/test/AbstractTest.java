package com.alphasystem.asciidoc.test;

import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static org.testng.Assert.fail;

import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.commons.SystemException;
import java.nio.file.Path;

public abstract class AbstractTest extends com.alphasystem.docbook.builder.test.AbstractTest {

  private static final String DATA_PATH = System.getProperty("data.path");

  public AbstractTest(String testTitle) {
    super(testTitle);
  }

  protected void processAdoc(String name) {
    final var sourcePath = get(DATA_PATH, format("%s.adoc", name));
    try {
      final var content =
          DocumentConverter.convertToDocBook(sourcePath).getDocumentInfo().getContent();
      System.out.println(content);
      unmarshallerTool.unmarshal(content);
    } catch (SystemException ex) {
      fail("Test failed", ex);
    }
  }

  protected String readAdoc(String name) {
    final var sourcePath = getDataPath(format("%s.adoc", name));
    try {
      return new String(java.nio.file.Files.readAllBytes(sourcePath));
    } catch (Exception ex) {
      fail(String.format("Fail to read file: %s", sourcePath.getFileName().toString()), ex);
    }
    return null;
  }

  protected Path getDataPath(String name) {
    return get(DATA_PATH, name);
  }
}
