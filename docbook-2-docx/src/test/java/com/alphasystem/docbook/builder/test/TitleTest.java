package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class TitleTest extends AbstractTest2 {

    @Test
    public void testTitle() {
        final var content = processContent(createArticle("Document title"));

        assertEquals(content.size(), 1);
        assertText(content.get(0), "Document title");
        addResult("Title test with default style", content);
    }

    @Test
    public void testTitleWithCustomStyle() {
        final var info = createInfo("Document title ", createPhrase("arabicHeading1", "سلم"));
        final var article = createArticle().withContent(info);
        final var content = processContent(article);

        assertEquals(content.size(), 1);
        assertText(content.get(0), "Document title سلم");
        addResult("Title test with custom style", content);
    }

    /*@Test(dependsOnMethods = "testDocumentTitleWithCustomStyle")
    public void testExampleTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Example(), -1);
        addResult(parent, 0, 3, "Example Title Test", createExample("Example Title"));
    }*/
}
