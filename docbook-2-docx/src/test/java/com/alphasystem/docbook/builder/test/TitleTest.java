package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;

/**
 * @author sali
 */
public class TitleTest extends AbstractTest2 {

    public TitleTest() {
        super("Titles");
    }

    @Test
    public void testTitle() {
        addTestTitle("Title test with default style");
        processContent(createArticle("Document title"));
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Document title");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTitle")
    public void testTitleWithCustomStyle() {
        addTestTitle("Title test with custom style");
        final var info = createInfo("Document title ", createPhrase("arabicHeading1", "سلم"));
        processContent(createArticle().withContent(info));
        final var content = mainDocumentPart.getContent();
        assertSize( 1);
        assertText(content.get(content.size() - 1), "Document title سلم");
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTitleWithCustomStyle")
    public void testSectionLevelTitles() {
        addTestTitle("Section Level title test");
        processContent(readXml("sections"));
        assertSize( 10);
        addHorizontalLine();
    }
}
