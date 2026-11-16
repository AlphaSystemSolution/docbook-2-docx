package com.alphasystem.asciidoc.test;

import static org.testng.Assert.assertEquals;

import org.docx4j.wml.Tbl;
import org.testng.annotations.Test;

/**
 * @author sali
 */
public class TableTest extends AbstractTest {

  public TableTest() {
    super("Tables");
  }

  @Test
  public void testInformalTable() {
    addTestTitle("Basic informal table test");
    processAdoc("informal-table");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.getLast()), 9);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testInformalTable")
  public void testSimpleTable() {
    addTestTitle("Basic table test");
    processAdoc("table");
    assertSize(2);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.getLast()), 6);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testSimpleTable")
  public void testTableVAlignMiddle() {
    addTestTitle("Basic table with \"center-left\" vertical align");
    processAdoc("table-center-left-align");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.getLast()), 4);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableVAlignMiddle")
  public void testTableVAlignBottom() {
    addTestTitle("Basic table with \"bottom-center\" vertical align");
    processAdoc("table-bottom-left-align");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableVAlignBottom")
  public void testTableVAlignCenter() {
    addTestTitle("Basic table with \"center-center\" align");
    processAdoc("table-center-center-align");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableVAlignCenter")
  public void testTableColumnSpan() {
    addTestTitle("Table with column span");
    processAdoc("table-column-span");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableColumnSpan")
  public void testTableRowSpan() {
    addTestTitle("Table with row span");
    processAdoc("table-row-span");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableRowSpan")
  public void testTableRowAndColumnSpan() {
    addTestTitle("Table with row and column span");
    processAdoc("table-row-column-span");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableRowAndColumnSpan")
  public void testComplexMultiRowColumnSpan() {
    addTestTitle("Test complex multi row and column span");
    processAdoc("table-complex-row-column-span");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testComplexMultiRowColumnSpan"})
  public void testTableWithoutBorder() {
    addTestTitle("Table without border");
    processAdoc("table-without-border");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testTableWithoutBorder"})
  public void testTableWithoutFrame() {
    addTestTitle("Table without frame");
    processAdoc("table-without-frame");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testTableWithoutFrame"})
  public void testTableWithoutRowSep() {
    addTestTitle("Table without row separator");
    processAdoc("table-without-row-sep");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testTableWithoutRowSep"})
  public void testTableWithoutColSep() {
    addTestTitle("Table without col separator");
    processAdoc("table-without-col-sep");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithoutColSep")
  public void testTableWithHeader() {
    addTestTitle("Table with header");
    processAdoc("table-with-header");
    assertSize(2);
    final var content = mainDocumentPart.getContent();
    // first header cell is a bare <literal> (wrapped in para), emitting an extra styled run
    assertEquals(getTableContentSize((Tbl) content.getLast()), 9);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithHeader")
  public void testTableWithFooter() {
    addTestTitle("Table with footer");
    processAdoc("table-with-footer");
    assertSize(2);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.getLast()), 8);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithFooter")
  public void testTableWithHeaderAndFooter() {
    addTestTitle("Table with header & footer");
    processAdoc("table-with-header-footer");
    assertSize(2);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.getLast()), 12);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithHeaderAndFooter")
  public void testTableWithStyle() {
    addTestTitle("Table with style (horizontal)");
    processAdoc("informal-table-with-style");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithStyle")
  public void testTableWithWidth() {
    addTestTitle("Table with table width");
    processAdoc("table-with-table-width");
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testTableWithWidth")
  public void testCustomCaption() {
    addTestTitle("Table with custom caption");
    processAdoc("table-with-custom-caption");
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testCustomCaption")
  public void testNestedTable() {
    addTestTitle("Nested Tables");
    processAdoc("nested-tables");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testNestedTable")
  public void testMultipleConsecutiveTables() {
    addTestTitle("Multiple consecutive tables");
    processAdoc("multiple-consecutive-tables");
    assertSize(3);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = "testMultipleConsecutiveTables")
  public void testTableWithAdmonition() {
    addTestTitle("Table with admonition");
    processAdoc("table-with-admonition");
    assertSize(1);
    addHorizontalLine();
  }
}
