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
        addTestTitle("Title test with default style");
        processContent(createArticle("Document title"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 2);
        assertText(content.get(content.size() - 1), "Document title");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTitle")
    public void testTitleWithCustomStyle() {
        addTestTitle("Title test with custom style");
        final var info = createInfo("Document title ", createPhrase("arabicHeading1", "سلم"));
        processContent(createArticle().withContent(info));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 2);
        assertText(content.get(content.size() - 1), "Document title سلم");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTitleWithCustomStyle")
    public void testSectionLevelTitles() {
        addTestTitle("Section Level title test");
        processContent(readXml("sections"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 11);
        addHorizontalLine();
    }

    /*@Test(dependsOnMethods = "testDocumentTitleWithCustomStyle")
    public void testExampleTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Example(), -1);
        addResult(parent, 0, 3, "Example Title Test", createExample("Example Title"));
    }*/
}
