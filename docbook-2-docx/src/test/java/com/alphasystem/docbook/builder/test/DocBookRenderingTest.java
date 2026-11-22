package com.alphasystem.docbook.builder.test;

import static org.testng.Assert.*;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.xml.DocBookModelNormalizer;
import com.alphasystem.xml.DocBookParser;
import com.alphasystem.xml.DocBookRenderer;
import com.alphasystem.xml.UnmarshallerTool;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.docx4j.XmlUtils;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DocBookRenderingTest extends AbstractTest {

  private List<Object> saved;
  private boolean previousTable;

  public DocBookRenderingTest() {
    super(null);
  }

  @BeforeMethod
  public void saveContent() {
    saved = new ArrayList<>(mainDocumentPart.getContent());
    previousTable = ApplicationController.getContext().isPreviousElementTable();
    mainDocumentPart.getContent().clear();
    ApplicationController.getContext().setPreviousElementTable(false);
  }

  @AfterMethod
  public void restoreContent() {
    mainDocumentPart.getContent().clear();
    mainDocumentPart.getContent().addAll(saved);
    ApplicationController.getContext().setPreviousElementTable(previousTable);
    updateCount();
  }

  @Test
  public void mixedInstructionsAndUnicode() throws Exception {
    convert(
        "<simpara>سلم <emphasis>one<?linebreak?>two</emphasis><?unknown anything?> three</simpara>");
    final var paragraph = mainDocumentPart.getContent().getFirst();
    final var xml = XmlUtils.marshaltoString(paragraph);
    assertTrue(xml.indexOf("one") < xml.indexOf("<w:br"), xml);
    assertTrue(xml.indexOf("<w:br") < xml.indexOf("two"), xml);
    assertTrue(xml.contains("سلم"), xml);
    assertTrue(xml.contains(" three"), xml);
    assertNull(ApplicationController.getContext().getDocBookRenderer());
  }

  @Test
  public void breaksBeforeTablesStayOutsideCells() throws Exception {
    convert("<?asciidoc-pagebreak?>" + table("", "cell"));
    final var content = mainDocumentPart.getContent();
    assertEquals(content.size(), 2);
    assertTrue(content.getFirst() instanceof P);
    assertTrue(((P) content.getFirst()).getPPr().getPageBreakBefore().isVal());
    assertTrue(content.getLast() instanceof Tbl);
    assertFalse(XmlUtils.marshaltoString(content.getLast()).contains("pageBreakBefore"));
  }

  @Test
  public void pageBreakInsideEmptyParagraphAppliesToFollowingContent() throws Exception {
    convert(
        "<simpara>before</simpara><simpara><?asciidoc-pagebreak?></simpara><simpara>after</simpara>");
    final var content = mainDocumentPart.getContent();
    assertEquals(content.size(), 2);
    assertText(content.getLast(), "after");
    assertTrue(((P) content.getLast()).getPPr().getPageBreakBefore().isVal());
  }

  @Test
  public void consecutiveAndTrailingBreaks() throws Exception {
    convert(
        "<?asciidoc-pagebreak?><?asciidoc-pagebreak?><simpara/><simpara>next</simpara><?asciidoc-pagebreak?>");
    final var content = mainDocumentPart.getContent();
    assertEquals(content.size(), 3);
    for (var object : content) {
      assertTrue(((P) object).getPPr().getPageBreakBefore().isVal());
    }
    assertText(content.get(1), "next");
    new UnmarshallerTool().unmarshal(article("<simpara>fresh</simpara>"));
    assertNull(((P) content.getLast()).getPPr().getPageBreakBefore());
  }

  @Test
  public void scopedTableWidthsAndQuotedAttributes() throws Exception {
    convert(
        table(
                "<?dbhtml other='ignored' table-width = '60%'?>",
                "outer" + table("<?dbhtml table-width=\"30%\"?>", "inner"))
            + table("", "following"));
    final var content = mainDocumentPart.getContent();
    final var outer = (Tbl) content.getFirst();
    final var following = (Tbl) content.getLast();
    assertEquals(outer.getTblPr().getTblW().getW(), BigInteger.valueOf(3000));
    assertEquals(following.getTblPr().getTblW().getW(), BigInteger.valueOf(5000));
    assertTrue(content.get(1) instanceof P);
    final var xml = XmlUtils.marshaltoString(outer);
    assertTrue(xml.contains("1500"), xml);
  }

  @Test
  public void schemaBoundExtensionNeedsOnlyBuilderRegistration() throws Exception {
    convert(
        "<simpara>before <code>one<?linebreak?>two</code> after</simpara>"
            + table("", "<code>cell<?linebreak?>code</code>"));
    final var paragraphXml = XmlUtils.marshaltoString(mainDocumentPart.getContent().getFirst());
    assertTrue(paragraphXml.contains("<w:br"), paragraphXml);
    final var tableXml = XmlUtils.marshaltoString(mainDocumentPart.getContent().getLast());
    assertTrue(tableXml.contains("<w:p"), tableXml);
    assertTrue(tableXml.contains("<w:br"), tableXml);
  }

  @Test
  public void lexicalReferencesAndLabels() throws Exception {
    convert(
        "<section xml:id='target' xreflabel='Custom'><title>Heading</title><simpara>body</simpara></section>"
            + "<simpara><xref linkend='target'/><link linkend='unresolved'>label</link></simpara>");
    final var xml = XmlUtils.marshaltoString(mainDocumentPart.getContent().getLast());
    assertTrue(xml.contains("anchor=\"target\""), xml);
    assertTrue(xml.contains("Custom"), xml);
    assertTrue(xml.contains("anchor=\"unresolved\""), xml);
  }

  @Test
  public void retainsInstructionsAfterNormalization() throws Exception {
    final var parsed =
        new DocBookParser().parse(article("<simpara>a<?linebreak?>b<?unknown c?></simpara>"));
    new DocBookModelNormalizer(parsed).normalize();
    assertEquals(parsed.getInstructions().size(), 2);
    assertEquals(parsed.getInstructions().getFirst().getPreviousSibling().getNodeValue(), "a");
    assertEquals(parsed.getInstructions().getFirst().getNextSibling().getNodeValue(), "b");
    assertNotNull(new DocBookRenderer(parsed));
  }

  @Test
  public void conversionFailureReleasesSession() {
    assertThrows(() -> new UnmarshallerTool().unmarshal(article("<unsupported/>")));
    assertNull(ApplicationController.getContext().getDocBookRenderer());
    assertEquals(ApplicationController.getContext().getTableWidth(), 0);
    assertFalse(ApplicationController.getContext().getKeepTogether());
  }

  private void convert(String body) throws Exception {
    new UnmarshallerTool().unmarshal(article(body));
  }

  private static String article(String body) {
    return "<article xmlns='http://docbook.org/ns/docbook'>" + body + "</article>";
  }

  private static String table(String instructions, String content) {
    return "<informaltable>"
        + instructions
        + "<tgroup cols='1'><colspec colname='col_1' colwidth='100*'/><tbody><row><entry>"
        + content
        + "</entry></row></tbody></tgroup></informaltable>";
  }
}
