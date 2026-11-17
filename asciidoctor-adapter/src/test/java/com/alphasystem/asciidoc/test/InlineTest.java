package com.alphasystem.asciidoc.test;

import static org.testng.Assert.*;

import org.docx4j.XmlUtils;
import org.docx4j.wml.Br;
import org.docx4j.wml.P;
import org.testng.annotations.Test;

public class InlineTest extends AbstractTest {

  public InlineTest() {
    super("Inline Elements");
  }

  @Test
  public void testBasicInline() {
    addTestTitle("Basic inline test");
    processAdoc("basic-inline");
    final var content = mainDocumentPart.getContent();
    assertSize(8);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testBasicInline")
  public void customParaStyleTest() {
    addTestTitle("Custom para style Test");
    processAdoc("custom-para");
    final var content = mainDocumentPart.getContent();
    assertSize(3);
    assertText(content.get(content.size() - 3), "Paragraph with custom style.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "customParaStyleTest")
  public void externalLinkWithHrefWithNoLinkContentTest() {
    addTestTitle("External link with \"href\" Test");
    processAdoc("external-link-no-content");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to https://tdg.docbook.org/.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "externalLinkWithHrefWithNoLinkContentTest")
  public void externalLinkWithHrefWithLinkContentTest() {
    addTestTitle("External link with \"href\" and link content Test");
    processAdoc("external-link-with-content");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to TDG.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "externalLinkWithHrefWithLinkContentTest")
  public void internalLinkWithHrefWithNoLinkContentTest() {
    addTestTitle("Internal link with \"href\" Test");
    processAdoc("internal-link-no-content");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to #linked_section.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "internalLinkWithHrefWithNoLinkContentTest")
  public void internalLinkWithHrefWithLinkContentTest() {
    addTestTitle("Internal link with \"href\" and link content Test");
    processAdoc("internal-link-with-content");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to internal section.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "internalLinkWithHrefWithLinkContentTest")
  public void lineBreakWithAsciidocBr() {
    addTestTitle("Line break test");
    final var start = mainDocumentPart.getContent().size();
    processAdoc("linebreak");
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "After page break");
    assertPageBreak((P) content.getLast());
    assertSize(3);
    assertSame(content.get(start + 2), content.getLast());
    addHorizontalLine();
  }

  private void assertPageBreak(P paragraph) {
    assertFalse(paragraph.getContent().stream().anyMatch(Br.class::isInstance));
    assertNotNull(paragraph.getPPr());
    assertNotNull(paragraph.getPPr().getPageBreakBefore());
    assertTrue(paragraph.getPPr().getPageBreakBefore().isVal());
    try {
      final var restored =
          (P) XmlUtils.unwrap(XmlUtils.unmarshalString(XmlUtils.marshaltoString(paragraph)));
      assertTrue(restored.getPPr().getPageBreakBefore().isVal());
    } catch (Exception ex) {
      fail("Page break must survive XML round trip", ex);
    }
  }

  @Test(dependsOnMethods = "lineBreakWithAsciidocBr")
  public void testSingleSpace() {
    addTestTitle("Space between two elements");
    processAdoc("space-between-two-elements");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(
        content.getLast(), "Zayd ate chocolate at home yesterday happily because he was hungry.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testSingleSpace")
  public void testColorCode() {
    addTestTitle("Color code test");
    processAdoc("color-code");
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testColorCode")
  public void testInlineTestDocument() {
    addTestTitle("InlineTest document");
    processAdoc("InlineTest");
    addHorizontalLine();
  }
}
