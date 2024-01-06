package com.alphasystem.docbook.builder.test;

import org.docbook.model.*;
import org.docx4j.wml.Tbl;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static java.lang.String.format;
import static org.docbook.model.Align.LEFT;
import static org.docbook.model.BasicVerticalAlign.TOP;
import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class TableTest extends AbstractTest2 {

    @Test
    public void testInformalTable() {
        addTestTitle("Basic informal table test");
        processContent(readXml("informal-table"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 9);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testInformalTable")
    public void testSimpleTable() {
        addTestTitle("Basic table test");
        processContent(readXml("table"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testSimpleTable")
    public void testTableVAlignMiddle() {
        addTestTitle("Basic table with \"center-left\" vertical align");
        processContent(readXml("table-center-left-align"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignMiddle")
    public void testTableVAlignBottom() {
        addTestTitle("Basic table with \"bottom-center\" vertical align");
        processContent(readXml("table-bottom-left-align"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignBottom")
    public void testTableVAlignCenter() {
        addTestTitle("Basic table with \"bottom-left\" vertical align");
        processContent(readXml("table-center-center-align"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableVAlignCenter")
    public void testTableColumnSpan() {
        addTestTitle("Table with column span");
        processContent(readXml("table-column-span"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 18);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableColumnSpan")
    public void testTableRowSpan() {
        addTestTitle("Table with row span");
        processContent(readXml("table-row-span"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 8);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableRowSpan")
    public void testTableRowAndColumnSpan() {
        addTestTitle("Table with row and column span");
        processContent(readXml("table-row-column-span"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = "testTableRowAndColumnSpan")
    public void testComplexMultiRowColumnSpan() {
        addTestTitle("Test complex multi row and column span");
        processContent(readXml("table-complex-row-column-span"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 31);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testComplexMultiRowColumnSpan"})
    public void testTableWithNoBorder() {
        final Table table = tableBorderTest(Frame.NONE, Choice.ZERO, Choice.ZERO);

        addTestTitle("Table without border");
        processContent(readXml("table-without-border"));
        assertSize(2);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 12);
        addHorizontalLine();
    }

    /*@Test(dependsOnMethods = "testTableVAlignBottom")
    public void testTableWithHeader() {
        final int numOfColumns = 4;
        final TableBody tableBody = createTableBody(null, null, _createRow(numOfColumns, 1));
        final TableGroup tableGroup = createTableGroup(_createHeader(numOfColumns), tableBody, null, 15, 15, 15, 55);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Header Test", table);
    }

    @Test(dependsOnMethods = "testTableWithHeader")
    public void testTableWithFooter() {
        final int numOfColumns = 4;
        final TableBody tableBody = createTableBody(null, null, _createRow(numOfColumns, 1));
        final TableGroup tableGroup = createTableGroup(null, tableBody, _createFooter(numOfColumns), 15, 15, 15, 55);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Footer Test", table);
    }

    @Test(dependsOnMethods = "testTableWithFooter")
    public void testTableWithHeaderAndFooter() {
        final int numOfColumns = 4;
        final TableBody tableBody = createTableBody(null, null, _createRow(numOfColumns, 1));
        final TableGroup tableGroup = createTableGroup(_createHeader(numOfColumns), tableBody,
                _createFooter(numOfColumns), 15, 15, 15, 55);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Header And Footer Test", table);
    }








    @Test(dependsOnMethods = "testComplexMultiRowColumnSpan")
    public void testTableWithFrameAll() {
        final Table table = tableBorderTest(Frame.ALL, Choice.ZERO, Choice.ZERO);
        addResult(null, 0, 1, "Table With Frame ALL Test", table);
    }



    @Test(dependsOnMethods = {"testTableWithNoBorder"})
    public void testTableWithNoFrame() {
        final Table table = tableBorderTest(Frame.NONE, Choice.ONE, Choice.ONE);
        addResult(null, 0, 1, "Table With No Frame Test", table);
    }*/

    private Table tableBorderTest(Frame frame, Choice colSep, Choice rowSep) {
        final int numOfColumns = 4;
        final TableBody tableBody = createTableBody(null, null, _createRows(numOfColumns, 3));
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 25, 25, 25, 25);
        return createTable(null, frame, colSep, rowSep, null, tableGroup);
    }

    private Entry _createEntry(Align align, BasicVerticalAlign vAlign, String text) {
        return _createEntry(align, vAlign, null, null, null, text);
    }

    private Entry _createEntry(Align align, BasicVerticalAlign vAlign, String nameStart, String nameEnd, String moreRows,
                               String text) {
        return createEntry(align, vAlign, nameStart, nameEnd, moreRows, createSimplePara(null, text));
    }

    private TableHeader _createHeader(int numOfColumns) {
        Object[] entries = new Entry[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            entries[i] = _createEntry(LEFT, TOP, format("Header %s", (i + 1)));
        }
        return createTableHeader(null, null, createRow(entries));
    }

    private TableFooter _createFooter(int numOfColumns) {
        Object[] entries = new Entry[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            entries[i] = _createEntry(LEFT, TOP, format("Footer %s", (i + 1)));
        }
        return createTableFooter(null, null, createRow(entries));
    }

    private Row[] _createRows(int numOfColumns, int numOfRows) {
        Row[] rows = new Row[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            rows[i] = _createRow(numOfColumns, (i + 1));
        }
        return rows;
    }

    private Row _createRow(int numOfColumns, int row) {
        return _createRow(numOfColumns, row, null);
    }

    private Row _createRow(int numOfColumns, int row, BasicVerticalAlign verticalAlign) {
        verticalAlign = (verticalAlign == null) ? TOP : verticalAlign;
        Object[] entries = new Entry[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            entries[i] = _createEntry(LEFT, verticalAlign, format("Row %s, Column %s", row, (i + 1)));
        }
        return createRow(entries);
    }
}
