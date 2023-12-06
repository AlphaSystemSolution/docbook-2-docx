package com.alphasystem.docbook.builder.test;

import org.docbook.model.*;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static com.alphasystem.util.IdGenerator.nextId;
import static java.lang.String.format;
import static org.docbook.model.Align.CENTER;
import static org.docbook.model.Align.LEFT;
import static org.docbook.model.BasicVerticalAlign.MIDDLE;
import static org.docbook.model.BasicVerticalAlign.TOP;

/**
 * @author sali
 */
public class TableTest extends AbstractTest {

    @Test
    public void testSimpleTable() {
        Entry entry1 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 1, row 1"));
        Entry entry2 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 2, row 1"));
        final Row row1 = createRow(entry1, entry2);

        entry1 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 1, row 2"));
        entry2 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 2, row 2"));
        final Row row2 = createRow(entry1, entry2);

        entry1 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 1, row 3"));
        entry2 = createEntry(LEFT, TOP, createSimplePara(null, "Cell in column 2, row 3"));
        final Row row3 = createRow(entry1, entry2);

        final TableBody tableBody = createTableBody(null, null, row1, row2, row3);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 50, 50);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, createTitle("Result: Rendered simple table"), tableGroup);

        addResult(null, 0, 2, "Simple Table Test", table);
    }

    @Test(dependsOnMethods = "testSimpleTable")
    public void testTableVAlignMiddle() {
        final int numOfColumns = 2;
        final BasicVerticalAlign verticalAlign = MIDDLE;
        final TableBody tableBody = createTableBody(null, null, _createRow(numOfColumns, 1, verticalAlign),
                _createRow(numOfColumns, 2, verticalAlign));
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 50, 50);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);

        addResult(null, 0, 1, "Table Test With Vertical Align Middle", table);
    }

    @Test(dependsOnMethods = "testTableVAlignMiddle")
    public void testTableVAlignBottom() {
        final int numOfColumns = 2;
        final BasicVerticalAlign verticalAlign = BasicVerticalAlign.BOTTOM;
        final TableBody tableBody = createTableBody(null, null, _createRow(numOfColumns, 1, verticalAlign),
                _createRow(numOfColumns, 2, verticalAlign));
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 50, 50);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table Test With Vertical Align Bottom", table);
    }

    @Test(dependsOnMethods = "testTableVAlignBottom")
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

    @Test(dependsOnMethods = "testTableWithHeaderAndFooter")
    public void testTableColumnSpan() {
        Entry entry1 = _createEntry(LEFT, TOP, "col_1", "col_4", null, "Row 1, Column Span 1 - 4");
        final Row row1 = createRow(entry1);

        entry1 = _createEntry(LEFT, TOP, "col_1", "col_2", null, "Row 2, Column Span 1 - 2");
        Entry entry2 = _createEntry(LEFT, TOP, "col_3", "col_4", null, "Row 2, Column Span 3 - 4");
        final Row row2 = createRow(entry1, entry2);

        entry1 = _createEntry(LEFT, TOP, "Row 3, Column 1");
        entry2 = _createEntry(LEFT, TOP, "col_2", "col_4", null, "Row 3, Column Span 2 - 4");
        final Row row3 = createRow(entry1, entry2);

        entry1 = _createEntry(LEFT, TOP, "col_1", "col_2", null, "Row 4, Column Span 1 - 2");
        entry2 = _createEntry(LEFT, TOP, "Row 4, Column 3");
        Entry entry3 = _createEntry(LEFT, TOP, "Row 4, Column 4");
        final Row row4 = createRow(entry1, entry2, entry3);

        entry1 = _createEntry(LEFT, TOP, "Row 5, Column 1");
        entry2 = _createEntry(LEFT, TOP, "col_2", "col_3", null, "Row 5, Column Span 2 - 3");
        entry3 = _createEntry(LEFT, TOP, "Row 5, Column 4");
        final Row row5 = createRow(entry1, entry2, entry3);

        entry1 = _createEntry(LEFT, TOP, "Row 6, Column 1");
        entry2 = _createEntry(LEFT, TOP, "Row 6, Column 2");
        entry3 = _createEntry(LEFT, TOP, "col_3", "col_4", null, "Row 6, Column Span 3 - 4");
        final Row row6 = createRow(entry1, entry2, entry3);

        final Row row7 = _createRow(4, 7);

        final TableBody tableBody = createTableBody(null, null, row1, row2, row3, row4, row5, row6, row7);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 25, 20, 35, 20);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Column Span Test", table);
    }

    @Test(dependsOnMethods = "testTableColumnSpan")
    public void testTableRowSpan() {
        final Row row1 = _createRow(4, 1);

        Entry entry1 = _createEntry(LEFT, TOP, null, null, "1", "Row 2, Column 1, Row Span 1");
        Entry entry2 = _createEntry(LEFT, TOP, "Row 2, Column 2");
        Entry entry3 = _createEntry(LEFT, TOP, "Row 2, Column 3");
        Entry entry4 = _createEntry(LEFT, TOP, "Row 2, Column 4");
        final Row row2 = createRow(entry1, entry2, entry3, entry4);

        entry2 = _createEntry(LEFT, TOP, "Row 3, Column 2");
        entry3 = _createEntry(LEFT, TOP, "Row 3, Column 3");
        entry4 = _createEntry(LEFT, TOP, "Row 3, Column 4");
        final Row row3 = createRow(entry2, entry3, entry4);

        final TableBody tableBody = createTableBody(null, null, row1, row2, row3);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 25, 25, 25, 25);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Row Span Test", table);
    }

    @Test(dependsOnMethods = "testTableRowSpan")
    public void testTableRowAndColumnSpan() {
        Entry entry1 = _createEntry(CENTER, MIDDLE, "col_1", "col_2", "2", "Row 1, Column 1 & 2");
        Entry entry2 = _createEntry(CENTER, MIDDLE, "col_2", "col_3", "2", "Row 1, Column 3 & 4");
        final Row row2 = createRow(entry1, entry2);

        final TableBody tableBody = createTableBody(null, null, row2);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 25, 25, 25, 25);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Row & Column Span Test", table);
    }

    @Test(dependsOnMethods = "testTableRowAndColumnSpan")
    public void testComplexMultiRowColumnSpan() {
        Entry entry1 = _createEntry(CENTER, MIDDLE, "col_1", "col_3", null, "");
        Entry middleEntry = _createEntry(CENTER, MIDDLE, null, null, "4", "");
        Entry entry2 = _createEntry(CENTER, MIDDLE, "col_3", "col_5", null, "");
        Entry entry3 = _createEntry(CENTER, MIDDLE, null, null, "1", "");
        final Row row1 = createRow(entry1, middleEntry, entry2, entry3);

        Object[] entries = new Entry[6];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = _createEntry(CENTER, MIDDLE, null, null, null, "");
        }
        final Row row2 = createRow(entries);

        entries = new Entry[7];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = _createEntry(CENTER, MIDDLE, null, null, null, "");
        }
        final Row row3 = createRow(entries);

        entries = new Entry[7];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = _createEntry(CENTER, MIDDLE, null, null, null, "");
        }
        final Row row4 = createRow(entries);

        entries = new Entry[7];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = _createEntry(CENTER, MIDDLE, null, null, null, "");
        }
        final Row row5 = createRow(entries);

        TableBody tableBody = createTableBody(null, null, row1, row2, row3, row4, row5);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 14, 14, 14, 1, 14, 14, 14, 15);
        final Table table = createTable(null, Frame.ALL, Choice.ONE, Choice.ONE, null, tableGroup);
        addResult(null, 0, 1, "Table With Row & Column Span Test", table);
    }

    @Test(dependsOnMethods = "testComplexMultiRowColumnSpan")
    public void testTableWithFrameAll() {
        final Table table = tableBorderTest(Frame.ALL, Choice.ZERO, Choice.ZERO);
        addResult(null, 0, 1, "Table With Frame ALL Test", table);
    }

    @Test(dependsOnMethods = {"testTableWithFrameAll"})
    public void testTableWithNoBorder() {
        final Table table = tableBorderTest(Frame.NONE, Choice.ZERO, Choice.ZERO);
        addResult(null, 0, 1, "Table With No Border Test", table);
    }

    @Test(dependsOnMethods = {"testTableWithNoBorder"})
    public void testTableWithNoFrame() {
        final Table table = tableBorderTest(Frame.NONE, Choice.ONE, Choice.ONE);
        addResult(null, 0, 1, "Table With No Frame Test", table);
    }

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
        return createEntry(align, vAlign, nameStart, nameEnd, moreRows, createSimplePara(nextId(), text));
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
