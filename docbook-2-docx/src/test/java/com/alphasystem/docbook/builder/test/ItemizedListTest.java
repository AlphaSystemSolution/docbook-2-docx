package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class ItemizedListTest extends AbstractTest2 {

    public ItemizedListTest() {
        super("Itemized list");
    }

    @Test
    public void testItemizedList() {
        addTestTitle("Un-Ordered list with \"diamond\" mark.");
        processContent(readXml("itemized-list"));
        assertSize(4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testItemizedList")
    public void testNestedItemizedList() {
        addTestTitle("Nested un-Ordered list with default marks.");
        processContent(readXml("itemized-list-nested"));
        assertSize(6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedItemizedList")
    public void testNestedMixedList() {
        addTestTitle("Nested mixed list");
        processContent(readXml("nested-mixed-list"));
        assertSize(10);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedItemizedList")
    public void testTableWithinList() {
        addTestTitle("Table within list");
        processContent(readXml("itemized-list-with-table"));
        assertSize(6);
        addHorizontalLine();
    }
}
