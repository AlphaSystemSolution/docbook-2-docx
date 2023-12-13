package com.alphasystem.docbook.builder.test;

import org.docbook.model.ListItem;
import org.docbook.model.OrderedList;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
public class OrderedListTest extends AbstractTest {

    @Test
    public void testOrderedList() {
        addResult(null, 0, 3, "OrderedList Test", readXml("orderedlist", OrderedList.class));
    }

    @Test(dependsOnMethods = "testOrderedList")
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
    }
}
