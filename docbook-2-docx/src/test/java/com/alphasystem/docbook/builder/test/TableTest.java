package com.alphasystem.docbook.builder.test;

import org.docx4j.wml.Tbl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class TableTest extends AbstractTest2 {

    public TableTest() {
        super("Tables");
    }

    @Test
    public void testInformalTable() {
        addTestTitle("Basic informal table test");
        processContent(readXml("informal-table"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 9);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testInformalTable")
    public void testSimpleTable() {
        addTestTitle("Basic table test");
        processContent(readXml("table"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testSimpleTable")
    public void testTableVAlignMiddle() {
        addTestTitle("Basic table with \"center-left\" vertical align");
        processContent(readXml("table-center-left-align"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignMiddle")
    public void testTableVAlignBottom() {
        addTestTitle("Basic table with \"bottom-center\" vertical align");
        processContent(readXml("table-bottom-left-align"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignBottom")
    public void testTableVAlignCenter() {
        addTestTitle("Basic table with \"bottom-left\" vertical align");
        processContent(readXml("table-center-center-align"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignCenter")
    public void testTableColumnSpan() {
        addTestTitle("Table with column span");
        processContent(readXml("table-column-span"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 18);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableColumnSpan")
    public void testTableRowSpan() {
        addTestTitle("Table with row span");
        processContent(readXml("table-row-span"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 8);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableRowSpan")
    public void testTableRowAndColumnSpan() {
        addTestTitle("Table with row and column span");
        processContent(readXml("table-row-column-span"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableRowAndColumnSpan")
    public void testComplexMultiRowColumnSpan() {
        addTestTitle("Test complex multi row and column span");
        processContent(readXml("table-complex-row-column-span"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 31);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testComplexMultiRowColumnSpan"})
    public void testTableWithoutBorder() {
        addTestTitle("Table without border");
        processContent(readXml("table-without-border"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 12);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testTableWithoutBorder"})
    public void testTableWithoutFrame() {
        addTestTitle("Table without frame");
        processContent(readXml("table-without-frame"));
        assertSize(1);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testTableWithoutFrame"})
    public void testTableWithoutRowSep() {
        addTestTitle("Table without row separator");
        processContent(readXml("table-without-row-sep"));
        assertSize(1);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testTableWithoutRowSep"})
    public void testTableWithoutColSep() {
        addTestTitle("Table without col separator");
        processContent(readXml("table-without-col-sep"));
        assertSize(1);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableWithoutColSep")
    public void testTableWithHeader() {
        addTestTitle("Table with header");
        processContent(readXml("table-with-header"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 8);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableWithHeader")
    public void testTableWithFooter() {
       addTestTitle("Table with footer");
        processContent(readXml("table-with-footer"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 8);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableWithFooter")
    public void testTableWithHeaderAndFooter() {
        addTestTitle("Table with header & footer");
        processContent(readXml("table-with-header-footer"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 12);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableWithHeaderAndFooter")
    public void testTableWithStyle() {
        addTestTitle("Table with style (horizontal)");
        processContent(readXml("informal-table-with-style"));
        assertSize(1);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 13);
        addHorizontalLine();
    }
}
