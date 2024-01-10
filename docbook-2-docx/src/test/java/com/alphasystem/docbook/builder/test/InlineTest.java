package com.alphasystem.docbook.builder.test;

import com.alphasystem.util.IdGenerator;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;

public class InlineTest extends AbstractTest2 {

    private final SimplePara paraWithXrefLabel = createSimplePara(IdGenerator.nextId(),
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
                        ", and some ",
                        createEmphasis("marked", "highlighted text"),
                        "."
                );

        addTestTitle("Basic inline test");
        processContent(createArticle(simplePara));
        final var content = mainDocumentPart.getContent();

        // validate
        assertSize( 1);
        assertText(content.get(content.size() - 1), "This paragraph contains some bold text, some italic text, and some highlighted text.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testBasicInline")
    public void testLiteralAndSubscriptTest() {
        final var subscript = createSubscript(IdGenerator.nextId(), "2");
        final var literal = createLiteral(IdGenerator.nextId(), "H", subscript, "O");
        final var article = createArticle(
                createSimplePara(
                        IdGenerator.nextId(),
                        "Chemical formula for water is ",
                        literal,
                        "."
                )
        );

        addTestTitle("Literal with subscript Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();

        // validate
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Chemical formula for water is H2O.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testBasicInline")
    public void testParaWithXrefLabel() {
        addTestTitle("Simple Paragraph with \"XREFLABEL\" Test");
        processContent(createArticle(paraWithXrefLabel));
        final var content = mainDocumentPart.getContent();

        // validate
        assertSize( 1);
        assertText(content.get(content.size() - 1), "A paragraph with xreflabel (Text to display).");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testParaWithXrefLabel")
    public void testSuperscriptTest() {
        final var superscript = createSuperscript(IdGenerator.nextId(), "2");
        final var phrase = createPhrase("strong", "E = mc", superscript);
        final var article = createArticle(
                createSimplePara(
                        IdGenerator.nextId(),
                        "Einstein's theory of relativity is ",
                        phrase,
                        "."
                )
        );

        addTestTitle("Superscript Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();

        // validate
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Einstein's theory of relativity is E = mc2.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testSuperscriptTest")
    public void mixedContentTest() {
        final var article = createArticle(
                createSimplePara(
                        IdGenerator.nextId(),
                        "This paragraph contains mixed of English and Arabic text (",
                        createPhrase("arabicNormal", "سلم"),
                        ")."
                )
        );

        addTestTitle("Mixed contents Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "This paragraph contains mixed of English and Arabic text (سلم).");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "mixedContentTest")
    public void multipleRolesTest() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(),
                        createPhrase("literal line-through green", "This text has multiple roles."),
                        " (literal, line-through, and green).")
        );

        addTestTitle("Multiple roles Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "This text has multiple roles. (literal, line-through, and green).");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "multipleRolesTest")
    public void customParaStyleTest() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(), "Paragraph with custom style.").withRole("Style1")
        );

        addTestTitle("Custom para style Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Paragraph with custom style.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "customParaStyleTest")
    public void xrefWithXrefLabel() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(), "Link to ", createCrossReference(paraWithXrefLabel), ".")
        );

        addTestTitle("XREF with xreflabel test");
        processContent(article);
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Link to Text to display.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "xrefWithXrefLabel")
    public void customParaStyleUsingParaTest() {
        final var article = createArticle(
                createPara(IdGenerator.nextId(), "Paragraph with custom style.").withRole("Style1")
        );

        addTestTitle("Custom para style using Para Test");
        processContent(article);
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Paragraph with custom style.");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "customParaStyleUsingParaTest")
    public void handleFormalWithDefaultTitleStylePara() {
        final var title = createTitle("Title with nested ", createEmphasis(null, "style"), ".");
        final var para = createPara(
                IdGenerator.nextId(),
                "This paragraph contains mixed of English and Arabic text (",
                createPhrase("arabicNormal", "سلم"),
                ")."
        );
        final var article = createArticle(
                createFormalPara(IdGenerator.nextId(), null, title, para)
        );

        addTestTitle("FormalPara test");
        processContent(article);
        assertSize( 2);
        addHorizontalLine();;
    }
}
