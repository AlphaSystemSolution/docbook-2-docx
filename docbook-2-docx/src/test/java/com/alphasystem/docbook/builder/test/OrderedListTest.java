package com.alphasystem.docbook.builder.test;

import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class OrderedListTest extends AbstractTest2 {

    @Test
    public void testOrderedList() {
        addTestTitle("Ordered list with \"arabic\" numeration.");
        processContent(readXml("ordered-list"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 5);

        mainDocumentPart.addObject(WmlAdapter.getParagraph("Numbering in the following list will be restarted."));
        processContent(readXml("ordered-list"));
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testOrderedList")
    public void testOrderedListLowerAlpha() {
        addTestTitle("Ordered list with \"loweralpha\" numeration.");
        processContent(readXml("ordered-list-lower-alpha"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testOrderedListLowerAlpha")
    public void testNestedOrderedList() {
        addTestTitle("Nested ordered list.");
        processContent(readXml("nested-ordered-list"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size(), previousSize + 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedOrderedList")
    public void testOrderedListMoreThenOnePara() {
        addTestTitle("Nested ordered list.");
        processContent(readXml("multi-para-ordered-list"));
        final var content = mainDocumentPart.getContent();
        assertEquals(content.size() - previousSize,4);
        addHorizontalLine();
    }
}
