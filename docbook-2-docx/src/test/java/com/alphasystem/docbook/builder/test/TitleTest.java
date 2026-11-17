package com.alphasystem.docbook.builder.test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.alphasystem.docbook.util.ConfigurationUtils;
import java.math.BigInteger;
import org.testng.annotations.Test;

/**
 * @author sali
 */
public class TitleTest extends AbstractTest {

  public TitleTest() {
    super("Titles");
  }

  @Test
  public void testNormalStyleFontSize() {
    final var normalStyle = mainDocumentPart.getStyleDefinitionsPart().getStyleById("Normal");
    final var fontSize = BigInteger.valueOf(ConfigurationUtils.getInstance().getDefaultFontSize());
    assertNotNull(normalStyle);
    assertNotNull(normalStyle.getRPr());
    assertNotNull(normalStyle.getRPr().getSz());
    assertNotNull(normalStyle.getRPr().getSzCs());
    assertEquals(normalStyle.getRPr().getSz().getVal(), fontSize);
    assertEquals(normalStyle.getRPr().getSzCs().getVal(), fontSize);
  }

  @Test
  public void testTitle() {
    addTestTitle("Title test with default style");
    processContent(createArticle("Document title"));
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(content.getLast(), "Document title");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTitle")
  public void testTitleWithCustomStyle() {
    addTestTitle("Title test with custom style");
    final var info = createInfo("Document title ", createPhrase("arabicHeading1", "سلم"));
    processContent(createArticle().withContent(info));
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(content.getLast(), "Document title سلم");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTitleWithCustomStyle")
  public void testSectionLevelTitles() {
    addTestTitle("Section Level title test");
    processContent(readXml("sections"));
    assertSize(10);
    addHorizontalLine();
  }
}
