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

    /*@Test(dependsOnMethods = "testOrderedList")
    public void testNestedOrderedList() {
        addResult(null, 0, 5, "OrderedList Test", readXml("nested-orderedlist", OrderedList.class));
    }

    @Test(dependsOnMethods = "testNestedOrderedList")
    public void testOrderedListMoreThenOnePara() {
        final SimplePara p1 = createSimplePara(nextId(), "First paragraph of list item, this should contain numeration.");
        final SimplePara p2 = createSimplePara(nextId(), "Second paragraph of list item, this should not contain numeration, but should be indented properly.");
        final ListItem li1 = createListItem(nextId(), p1, p2);
        final ListItem li2 = createListItem(nextId(), createSimplePara(null, "Second bullet point."));
        final OrderedList orderedList = createOrderedList(nextId(), li1, li2);
        addResult(null, 0, 3, "Ordered list with a list item having multiple para", orderedList);
    }*/
}
