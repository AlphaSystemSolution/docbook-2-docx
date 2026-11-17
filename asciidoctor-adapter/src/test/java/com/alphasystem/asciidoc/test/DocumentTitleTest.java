package com.alphasystem.asciidoc.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class DocumentTitleTest extends AbstractTest {

  public DocumentTitleTest() {
    super("Document Title");
  }

  @Test
  public void testDocumentTitle() {
    processAdoc("title");
    final var content = mainDocumentPart.getContent();
    assertSize(2);
    assertText(content.get(content.size() - 2), "Adapter Test Document");
    assertText(
        content.getLast(),
        "A simple paragraph to verify the asciidoc to docbook to docx pipeline.");
  }
}
