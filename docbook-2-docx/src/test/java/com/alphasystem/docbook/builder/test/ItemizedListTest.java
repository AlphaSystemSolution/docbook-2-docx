package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class ItemizedListTest extends AbstractTest {

    public ItemizedListTest() {
        super("Itemized list");
    }

    @Test
    public void testItemizedList() {
        addTestTitle("Un-Ordered list with \"diamond\" mark.");
        processContent(readXml("itemized-list"));
        assertSize(3);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testItemizedList")
    public void testNestedItemizedList() {
        addTestTitle("Nested un-Ordered list with default marks.");
        processContent(readXml("itemized-list-nested"));
        assertSize(5);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedItemizedList")
    public void testNestedMixedList() {
        addTestTitle("Nested mixed list");
        processContent(readXml("nested-mixed-list"));
        assertSize(9);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testNestedItemizedList")
    public void testTableWithinList() {
        addTestTitle("Table within list");
        processContent(readXml("itemized-list-with-table"));
        assertSize(5);
        addHorizontalLine();
    }
}
