package com.alphasystem.docbook.builder.test;

import org.docbook.model.ItemizedList;
import org.docbook.model.ListItem;
import org.docbook.model.SimplePara;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
public class ItemizedListTest extends AbstractTest {

    @Test
    public void testItemizedList() {
        addResult(null, 0, 8, "ItemizedList Test", readXml("itemizedlist", ItemizedList.class));
    }

    @Test(dependsOnMethods = "testItemizedList")
    public void testNestedItemizedList() {
        addResult(null, 0, 6, "ItemizedList Test", readXml("nested-itemizedlist", ItemizedList.class));
    }

    @Test(dependsOnMethods = "testNestedItemizedList")
    public void testItemizedListMoreThenOnePara() {
        final SimplePara p1 = createSimplePara(null, "First paragraph of list item, this should contain bullet mark.");
        final SimplePara p2 = createSimplePara(null, "Second paragraph of list item, this should not contain bullet mark, but should be indented properly.");
        final ListItem li1 = createListItem(null, p1, p2);
        final ListItem li2 = createListItem(null, createSimplePara(null, "Second bullet point."));
        final ItemizedList itemizedList = createItemizedList(nextId(), li1, li2);
        addResult(null, 0, 3, "Itemized list with a list item having multiple para", itemizedList);
    }
}
