package com.alphasystem.docbook.builder.test;

import com.alphasystem.SystemException;
import com.alphasystem.util.IdGenerator;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static org.testng.Assert.assertEquals;

public class InlineTest extends AbstractTest2 {

    private final SimplePara paraWithXrefLabel = createSimplePara(IdGenerator.nextId(),
            "A paragraph with ",
            createLiteral(IdGenerator.nextId(), "xreflabel"),
            " (",
            createEmphasis(null, "Text to display"),
            ").")
            .withXreflabel("Text to display");

    @Test
    public void testBasicInline() throws SystemException {
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

        final var article = createArticle(simplePara);
        final var content = processContent(article);

        // validate
        assertEquals(content.size(), 1);
        assertText(content.get(0), "This paragraph contains some bold text, some italic text, and some highlighted text.");
        addResult("Basic inline test", content);
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
        final var content = processContent(article);

        // validate
        assertEquals(content.size(), 1);
        assertText(content.get(0), "Chemical formula for water is H2O.");
        addResult("Literal with subscript Test", content);
    }

    @Test(dependsOnMethods = "testBasicInline")
    public void testParaWithXrefLabel() {
        final var content = processContent(createArticle(paraWithXrefLabel));

        // validate
        assertEquals(content.size(), 1);
        assertText(content.get(0), "A paragraph with xreflabel (Text to display).");

        addResult("Simple Paragraph with \"XREFLABEL\" Test", content);
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
        final var content = processContent(article);

        // validate
        assertEquals(content.size(), 1);
        assertText(content.get(0), "Einstein's theory of relativity is E = mc2.");

        addResult("Superscript Test", content);
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
        final var content = processContent(article);
        assertEquals(content.size(), 1);
        assertText(content.get(0), "This paragraph contains mixed of English and Arabic text (سلم).");
        addResult("Mixed contents Test", content);
    }

    @Test(dependsOnMethods = "mixedContentTest")
    public void multipleRolesTest() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(),
                        createPhrase("literal line-through green", "This text has multiple roles."),
                        " (literal, line-through, and green).")
        );
        final var content = processContent(article);
        assertEquals(content.size(), 1);
        assertText(content.get(0), "This text has multiple roles. (literal, line-through, and green).");
        addResult("Multiple roles Test", content);
    }

    @Test(dependsOnMethods = "multipleRolesTest")
    public void customParaStyleTest() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(), "Paragraph with custom style.").withRole("Style1")
        );
        final var content = processContent(article);
        assertEquals(content.size(), 1);
        assertText(content.get(0), "Paragraph with custom style.");
        addResult("Custom para style Test", content);
    }

    @Test(dependsOnMethods = "customParaStyleTest")
    public void xrefWithXrefLabel() {
        final var article = createArticle(
                createSimplePara(IdGenerator.nextId(), "Link to ", createCrossReference(paraWithXrefLabel), ".")
        );
        final var content = processContent(article);
        assertEquals(content.size(), 1);
        assertText(content.get(0), "Link to Text to display.");
        addResult("XREF with xreflabel test", content);
    }
}
