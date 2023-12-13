package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Article;
import org.docbook.model.Literal;
import org.docbook.model.Phrase;
import org.docbook.model.SimplePara;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static com.alphasystem.util.IdGenerator.nextId;
import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class InlineTest extends AbstractTest {

    @Test
    public void testBold() {
        final List<Object> content = buildContent(null, -1, createBold("Bold Text"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Bold Test", r);
    }

    @Test(dependsOnMethods = "testBold")
    public void testItalic() {
        final List<Object> content = buildContent(null, -1, createItalic("Italic Text"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Italic Test", r);
    }

    @Test(dependsOnMethods = "testItalic")
    public void testHighlight() {
        final var parent = builderFactory.getBuilder(null, new Article(), 0);
        final var content = buildContent(parent, -1, createHighlight(nextId(), "highlighted text"));
        assertEquals(content.size(), 1);
        final var p = (P) content.get(0);
        addResultToDocument("Highlight Test", p);
    }

    @Test(dependsOnMethods = "testHighlight")
    public void testTermBuilder() {
        final List<Object> content = buildContent(null, -1, createTerm("Term Title"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Term Test", r);
    }

    @Test(dependsOnMethods = "testTermBuilder")
    public void testSubscript() {
        final Literal literal = createLiteral(null, "H", createSubscript(null, "2"), "O");
        final Phrase phrase = createPhrase("bold", literal);
        final List<Object> content = buildContent(null, -1, "Chemical formula for water is ", phrase);
        assertEquals(content.size(), 4);
        addResult("Subscript Test", convertToRuns(content));
    }

    @Test(dependsOnMethods = "testSubscript")
    public void testSuperscript() {
        final Literal literal = createLiteral(null, "E = mc", createSuperscript(null, "2"));
        final Phrase phrase = createPhrase("bold", literal);
        final List<Object> content = buildContent(null, -1, "Einstein's theory of relativity is ", phrase);
        assertEquals(content.size(), 3);
        addResult("Superscript Test", convertToRuns(content));
    }

    @Test(dependsOnMethods = "testSuperscript")
    public void testMixedArabicEnglishText() {
        final SimplePara simplePara = createSimplePara(nextId(), "This text has mixed English and Arabic (",
                createPhrase("arabicNormal", "س ل م"), ") text.");
        final Builder parent = builderFactory.getBuilder(null, new Article(), 0);
        final List<Object> content = buildContent(parent, -1, simplePara);
        assertEquals(content.size(), 1);
        addResultToDocument("Mixed Text", content.toArray());
    }

    @Test(dependsOnMethods = "testMixedArabicEnglishText")
    public void testArabicPara() {
        final SimplePara simplePara = createSimplePara(nextId(), "س ل م").withRole("Style1");
        final Builder parent = builderFactory.getBuilder(null, new Article(), 0);
        final List<Object> content = buildContent(parent, -1, simplePara);
        assertEquals(content.size(), 1);
        addResultToDocument("Arabic Para", content.toArray());
    }
}
