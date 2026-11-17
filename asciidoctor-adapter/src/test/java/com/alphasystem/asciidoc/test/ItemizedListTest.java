package com.alphasystem.asciidoc.test;

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
    addTestTitle("Un-Ordered list.");
    processAdoc("itemized-list");
    assertSize(3);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testItemizedList")
  public void testNestedItemizedList() {
    addTestTitle("Nested un-Ordered list with default marks.");
    processAdoc("itemized-list-nested");
    assertSize(5);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testNestedItemizedList")
  public void testNestedMixedList() {
    addTestTitle("Nested mixed list");
    processAdoc("nested-mixed-list");
    assertSize(9);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testNestedMixedList")
  public void testTableWithinList() {
    addTestTitle("Table within list");
    processAdoc("itemized-list-with-table");
    assertSize(5);
    addHorizontalLine();
  }
}
