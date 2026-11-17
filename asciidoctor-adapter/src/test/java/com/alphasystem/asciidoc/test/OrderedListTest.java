package com.alphasystem.asciidoc.test;

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
    processAdoc("ordered-list");
    assertSize(4);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testOrderedList")
  public void testOrderedListLowerAlpha() {
    addTestTitle("Ordered list with \"loweralpha\" numeration.");
    processAdoc("ordered-list-lower-alpha");
    assertSize(3);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testOrderedListLowerAlpha")
  public void testNestedOrderedList() {
    addTestTitle("Nested ordered list.");
    processAdoc("ordered-list-nested");
    assertSize(5);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testNestedOrderedList")
  public void testOrderedListMoreThenOnePara() {
    addTestTitle("Ordered list with multiple paragraphs in a list item.");
    processAdoc("ordered-list-multi-para");
    assertSize(3);
    addHorizontalLine();
  }
}
