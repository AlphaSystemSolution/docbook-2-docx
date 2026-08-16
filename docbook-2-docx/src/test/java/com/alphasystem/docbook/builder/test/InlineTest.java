package com.alphasystem.docbook.builder.test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;

import com.alphasystem.commons.util.IdGenerator;
import java.util.ArrayList;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;

public class InlineTest extends AbstractTest {

  private final SimplePara paraWithXrefLabel =
      createSimplePara(
              "linked_section",
              "A paragraph with ",
              createLiteral(IdGenerator.nextId(), "xreflabel"),
              " (",
              createEmphasis(null, "Text to display"),
              ").")
          .withXreflabel("Text to display");

  public InlineTest() {
    super("Inline Elements");
  }

  @Test
  public void testBasicInline() {
    final var simplePara =
        createSimplePara(
            IdGenerator.nextId(),
            "This paragraph contains some ",
            createBold("bold text"),
            ", some ",
            createEmphasis(null, "italic text"),
            ", and ",
            createEmphasis("marked", "default highlighted text"),
            ", and few highlighted texts with custom colors: ",
            createPhrase("highlight-red", "Red"),
            ", ",
            createPhrase("highlight-cyan", "Cyan"),
            ", ",
            createPhrase("highlight-magenta", "Magenta"),
            ", ",
            createPhrase("highlight-green", "Green"),
            ".");

    addTestTitle("Basic inline test");
    processContent(createArticle(simplePara));
    final var content = mainDocumentPart.getContent();

    // validate
    assertSize(1);
    assertText(
        content.getLast(),
        "This paragraph contains some bold text, some italic text, "
            + "and default highlighted text, and few highlighted texts with custom colors: Red, Cyan, Magenta, Green.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testBasicInline")
  public void testLiteralAndSubscriptTest() {
    final var subscript = createSubscript(IdGenerator.nextId(), "2");
    final var literal = createLiteral(IdGenerator.nextId(), "H", subscript, "O");
    final var article =
        createArticle(
            createSimplePara(IdGenerator.nextId(), "Chemical formula for water is ", literal, "."));

    addTestTitle("Literal with subscript Test");
    processContent(article);
    final var content = mainDocumentPart.getContent();

    // validate
    assertSize(1);
    assertText(content.getLast(), "Chemical formula for water is H2O.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testLiteralAndSubscriptTest")
  public void testParaWithXrefLabel() {
    addTestTitle("Simple Paragraph with \"XREFLABEL\" Test");
    processContent(createArticle(paraWithXrefLabel));
    final var content = mainDocumentPart.getContent();

    // validate
    assertSize(1);
    assertText(content.getLast(), "A paragraph with xreflabel (Text to display).");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testParaWithXrefLabel")
  public void testSuperscriptTest() {
    final var superscript = createSuperscript(IdGenerator.nextId(), "2");
    final var phrase = createPhrase("strong", "E = mc", superscript);
    final var article =
        createArticle(
            createSimplePara(
                IdGenerator.nextId(), "Einstein's theory of relativity is ", phrase, "."));

    addTestTitle("Superscript Test");
    processContent(article);
    final var content = mainDocumentPart.getContent();

    // validate
    assertSize(1);
    assertText(content.getLast(), "Einstein's theory of relativity is E = mc2.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testSuperscriptTest")
  public void mixedContentTest() {
    final var article =
        createArticle(
            createSimplePara(
                IdGenerator.nextId(),
                "This paragraph contains mixed of English and Arabic text (",
                createPhrase("arabicNormal", "سلم"),
                ")."));

    addTestTitle("Mixed contents Test");
    processContent(article);
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(
        content.getLast(), "This paragraph contains mixed of English and Arabic text (سلم).");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "mixedContentTest")
  public void multipleRolesTest() {
    final var article =
        createArticle(
            createSimplePara(
                IdGenerator.nextId(),
                createPhrase("literal line-through green", "This text has multiple roles."),
                " (literal, line-through, and green)."));

    addTestTitle("Multiple roles Test");
    processContent(article);
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(
        content.getLast(), "This text has multiple roles. (literal, line-through, and green).");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "multipleRolesTest")
  public void customParaStyleTest() {
    addTestTitle("Custom para style Test");
    processContent(readXml("custom-para"));
    final var content = mainDocumentPart.getContent();
    assertSize(3);
    assertText(content.get(content.size() - 3), "Paragraph with custom style.");
    assertText(content.get(content.size() - 2), "نَصَرَ يَنْصُرُ نَصْرًا فهو نَاصِرٌ");
    assertText(content.getLast(), "نَصَرَ يَنْصُرُ نَصْرًا فهو نَاصِرٌ");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "customParaStyleTest")
  public void xrefWithXrefLabel() {
    final var article =
        createArticle(
            createSimplePara(
                IdGenerator.nextId(), "Link to ", createCrossReference(paraWithXrefLabel), "."));

    addTestTitle("XREF with \"reflabel\" test");
    processContent(article);
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(content.getLast(), "Link to Text to display.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "xrefWithXrefLabel")
  public void linkWithLinkEndTest() {
    final var link = createLink(paraWithXrefLabel, null, null);
    final var article =
        createArticle(createSimplePara(IdGenerator.nextId(), "Link to ", link, "."));

    addTestTitle("Link with \"linkEnd\" Test");
    processContent(article);
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to Text to display.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "linkWithLinkEndTest")
  public void externalLinkWithHrefWithNoLinkContentTest() {
    addTestTitle("External link with \"href\" Test");
    processContent(readXml("external-link-no-content"));
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to https://tdg.docbook.org/.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "externalLinkWithHrefWithNoLinkContentTest")
  public void externalLinkWithHrefWithLinkContentTest() {
    addTestTitle("External link with \"href\" and link content Test");
    processContent(readXml("external-link-with-content"));
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to TDG.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "externalLinkWithHrefWithLinkContentTest")
  public void internalLinkWithHrefWithNoLinkContentTest() {
    addTestTitle("Internal link with \"href\" Test");
    processContent(readXml("internal-link-no-content"));
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to Text to display.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "internalLinkWithHrefWithNoLinkContentTest")
  public void internalLinkWithHrefWithLinkContentTest() {
    addTestTitle("Internal link with \"href\" and link content Test");
    processContent(readXml("internal-link-with-content"));
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(content.getLast(), "Link to internal section.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "internalLinkWithHrefWithLinkContentTest")
  public void customParaStyleUsingParaTest() {
    final var article =
        createArticle(
            createPara(IdGenerator.nextId(), "Paragraph with custom style.").withRole("Style1"));

    addTestTitle("Custom para style using Para Test");
    processContent(article);
    final var content = mainDocumentPart.getContent();
    assertSize(1);
    assertText(content.getLast(), "Paragraph with custom style.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "customParaStyleUsingParaTest")
  public void handleFormalWithDefaultTitleStylePara() {
    final var title = createTitle("Title with nested ", createEmphasis(null, "style"), ".");
    final var para =
        createPara(
            IdGenerator.nextId(),
            "This paragraph contains mixed of English and Arabic text (",
            createPhrase("arabicNormal", "سلم"),
            ").");
    final var article = createArticle(createFormalPara(IdGenerator.nextId(), null, title, para));

    addTestTitle("FormalPara test");
    processContent(article);
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "handleFormalWithDefaultTitleStylePara")
  public void lineBreakWithAsciidocBr() {
    addTestTitle("Line break test");
    processContent(readXml("linebreak"));
    assertSize(4);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "lineBreakWithAsciidocBr")
  public void testSingleSpace() {
    addTestTitle("Space between two elements");
    processContent(readXml("space-between-two-elements"));
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertText(
        content.getLast(), "Zayd ate chocolate at home yesterday happily because he was hungry.");
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testSingleSpace")
  public void testColorCode() {
    addTestTitle("Color code test");
    processContent(readXml("color-code"));
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testColorCode")
  public void testTextAlignment() {
    addTestTitle("Text alignment test");

    var contents = new ArrayList<Object>();

    var simplePara = createSimplePara(IdGenerator.nextId(), createBold("Default alignment"));
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), "Normal text");
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), createBold("Justified alignment"));
    contents.add(simplePara);

    simplePara =
        createSimplePara(
            IdGenerator.nextId(),
            createBold("Lorem Ipsum "),
            "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever "
                + "since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. "
                + "It has survived not only five centuries, but also the leap into electronic typesetting, "
                + "remaining essentially unchanged. It was popularized in the 1960s with the release of Letraset sheets containing "
                + "Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions "
                + "of Lorem Ipsum.");
    simplePara.setRole("text-justify");
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), createBold("Right alignment"));
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), createPhrase("arabicNormal", "فعل"));
    simplePara.setRole("text-right");
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), "Center alignment");
    contents.add(simplePara);

    simplePara = createSimplePara(IdGenerator.nextId(), createBold("Centered text"));
    simplePara.setRole("text-center");
    contents.add(simplePara);

    processContent(createArticle(contents.toArray()));
    assertSize(8);
    addHorizontalLine();
  }
}
