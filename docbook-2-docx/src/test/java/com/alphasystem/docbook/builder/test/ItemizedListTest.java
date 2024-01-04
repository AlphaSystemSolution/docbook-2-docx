package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class ItemizedListTest extends AbstractTest2 {

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
        processContent(readXml("nested-itemized-list"));
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
}
