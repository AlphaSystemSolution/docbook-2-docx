package com.alphasystem.docbook.builder.test;

import static org.testng.Assert.*;

import com.alphasystem.xml.DocBookParser;
import org.docbook.model.Article;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

public class DocBookParserTest {

  @Test
  public void bindsObjectsWithoutLosingInstructions() throws Exception {
    final var parsed =
        new DocBookParser()
            .parse(
                """
        <?before value="one"?>
        <article xmlns="http://docbook.org/ns/docbook" xml:id="root">
          <simpara xml:id="p">before<?linebreak?>after <emphasis>styled<?custom data?>text</emphasis></simpara>
        </article>
        <?after value="two"?>
        """);
    assertTrue(parsed.getRoot() instanceof Article);
    final var paragraphs =
        parsed.getDocument().getElementsByTagNameNS("http://docbook.org/ns/docbook", "simpara");
    final var element = paragraphs.item(0);
    assertTrue(parsed.getObject(element) instanceof SimplePara);
    assertSame(parsed.getNode(parsed.getObject(element)), element);
    assertEquals(parsed.getInstructions().size(), 4);
    assertEquals(parsed.getInstructions().get(1).getTarget(), "linebreak");
    assertEquals(element.getFirstChild().getNodeValue(), "before");
    assertEquals(
        element.getFirstChild().getNextSibling().getNodeType(), Node.PROCESSING_INSTRUCTION_NODE);
    assertEquals(parsed.getInstructions().get(2).getData(), "data");
  }

  @Test
  public void bindsReferencesAndTableStructures() throws Exception {
    final var parsed =
        new DocBookParser()
            .parse(
                """
        <article xmlns="http://docbook.org/ns/docbook" xml:id="root">
          <simpara><xref linkend="target"/><link linkend="missing">label</link></simpara>
          <section xml:id="target"><title>Target</title>
            <informaltable><tgroup cols="1"><tbody><row><entry>cell</entry></row></tbody></tgroup></informaltable>
          </section>
        </article>
        """);
    for (String name :
        new String[] {
          "article",
          "section",
          "title",
          "xref",
          "link",
          "informaltable",
          "tgroup",
          "tbody",
          "row",
          "entry"
        }) {
      final var element =
          parsed
              .getDocument()
              .getElementsByTagNameNS("http://docbook.org/ns/docbook", name)
              .item(0);
      assertNotNull(parsed.getObject(element), name);
      assertSame(parsed.getNode(parsed.getObject(element)), element, name);
    }
  }

  @Test
  public void preservesUnicodeAndCdata() throws Exception {
    final var parsed =
        new DocBookParser()
            .parse(
                """
        <article xmlns="http://docbook.org/ns/docbook"><simpara>سلم <![CDATA[<text>]]></simpara></article>
        """);
    assertEquals(parsed.getDocument().getDocumentElement().getTextContent(), "سلم <text>");
  }

  @Test(expectedExceptions = org.xml.sax.SAXParseException.class)
  public void rejectsExternalEntities() throws Exception {
    new DocBookParser()
        .parse(
            """
        <!DOCTYPE article [<!ENTITY external SYSTEM "file:///not-readable">]>
        <article xmlns="http://docbook.org/ns/docbook"><simpara>&external;</simpara></article>
        """);
  }
}
