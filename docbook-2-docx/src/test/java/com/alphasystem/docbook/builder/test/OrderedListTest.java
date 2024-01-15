package com.alphasystem.docbook.builder.test;

import com.alphasystem.openxml.builder.wml.WmlAdapter;
import org.testng.annotations.Test;

/**
 * @author sali
 */
public class OrderedListTest extends AbstractTest {

    public OrderedListTest() {
        super("Ordered List");
    }

    @Test
    public void testOrderedList() {
        addTestTitle("Ordered list with \"arabic\" numeration.");
        processContent(readXml("ordered-list"));
        assertSize(4);

        mainDocumentPart.addObject(WmlAdapter.getParagraph("Numbering in the following list will be restarted."));
        processContent(readXml("ordered-list"));
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testOrderedList")
    public void testOrderedListLowerAlpha() {
        addTestTitle("Ordered list with \"loweralpha\" numeration.");
        processContent(readXml("ordered-list-lower-alpha"));
        assertSize(3);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testOrderedListLowerAlpha")
    public void testNestedOrderedList() {
        addTestTitle("Nested ordered list.");
        processContent(readXml("ordered-list-nested"));
        assertSize(5);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedOrderedList")
    public void testOrderedListMoreThenOnePara() {
        addTestTitle("Nested ordered list.");
        processContent(readXml("ordered-list-multi-para"));
        assertSize(3);
        addHorizontalLine();
    }
}
