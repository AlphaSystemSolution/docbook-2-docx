package com.alphasystem.asciidoc.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class VariableListTest extends AbstractTest {

  public VariableListTest() {
    super("Variable List");
  }

  @Test
  public void testBasicVariableList() {
    addTestTitle("Basic variable list test");
    processAdoc("variable-list");
    assertSize(12);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testBasicVariableList")
  public void testVariableListWithMultipleTerms() {
    addTestTitle("Basic variable list with multiple terms and complex list item test");
    processAdoc("variable-list-multiple-terms");
    assertSize(4);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testVariableListWithMultipleTerms")
  public void testBasicHybridVariableList() {
    addTestTitle("Basic hybrid variable list test");
    processAdoc("variable-list-basic-hybrid");
    assertSize(7);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testBasicHybridVariableList")
  public void testNestedHybridVariableList() {
    addTestTitle("Nested hybrid variable list test");
    processAdoc("variable-list-nested-hybrid");
    assertSize(17);
    addHorizontalLine();
  }
}
