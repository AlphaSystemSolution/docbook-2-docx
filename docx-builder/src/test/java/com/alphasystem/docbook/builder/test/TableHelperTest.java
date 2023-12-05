package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.util.TableHelper;
import com.alphasystem.openxml.builder.wml.table.ColumnInfo;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import com.alphasystem.openxml.builder.wml.table.TableType;
import org.docbook.model.ColumnSpec;
import org.docbook.model.ObjectFactory;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
public class TableHelperTest {

    private static final BigDecimal TOTAL_GRID_COL_WIDTH = TableAdapter.TOTAL_GRID_COL_WIDTH;
    private static final BigDecimal TOTAL_TABLE_WIDTH = TableAdapter.TOTAL_TABLE_WIDTH;
    private static final BigDecimal PERCENT = TableAdapter.PERCENT;
    private static final MathContext ROUNDING = TableAdapter.ROUNDING;
    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private static ColumnSpec createColumnSpec(String width) {
        return createColumnSpec(null, width);
    }

    private static ColumnSpec createColumnSpec(String name, String width) {
        return OBJECT_FACTORY.createColumnSpec().withColumnName(name).withColumnWidth(width);
    }

    private static void printColumnInfos(List<ColumnInfo> columnInfos) {
        log("#####################################################", true);
        columnInfos.forEach(TableHelperTest::printColumnInfo);
        log("#####################################################", true);
    }

    private static void printColumnInfo(ColumnInfo columnInfo) {
        log("************************************************", true);
        log(format("Column Number: %s", columnInfo.getColumnNumber()), true);
        log(format("Column Name: %s", columnInfo.getColumnName()), true);
        log(format("Column Width: %s", columnInfo.getColumnWidth()), true);
        log(format("Column Width%%: %s", getPercentage(columnInfo.getColumnWidth(), TOTAL_TABLE_WIDTH)), true);
        log(format("Grid Width: %s", columnInfo.getGridWidth()), true);
        log(format("Grid Width%%: %s", getPercentage(columnInfo.getGridWidth(), TOTAL_GRID_COL_WIDTH)), true);
        log("************************************************", true);
    }

    private static void verifyWidth(double width, BigDecimal totalWidth, double percent) {
        BigDecimal actual = new BigDecimal(percent);
        BigDecimal expected = getPercentage(width, totalWidth);
        assertEquals(actual.intValue(), expected.intValue());
    }

    private static BigDecimal getPercentage(double width, BigDecimal totalWidth) {
        return (new BigDecimal(width).multiply(PERCENT)).divide(totalWidth, ROUNDING);
    }

    private static void verifyWidth(ColumnInfo columnInfo, double percent) {
        verifyWidth(columnInfo.getColumnWidth(), TOTAL_TABLE_WIDTH, percent);
        verifyWidth(columnInfo.getGridWidth(), TOTAL_GRID_COL_WIDTH, percent);
    }

    @Test
    public void test1() {
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        columnSpecs.add(createColumnSpec("15*"));
        columnSpecs.add(createColumnSpec("85*"));

        final var columnAdapter = TableHelper.buildColumns(TableType.PCT, 0, columnSpecs);
        final var columnInfos = columnAdapter.getColumns();
        printColumnInfos(columnInfos);
        verifyWidth(columnInfos.get(0), 15.0);
        verifyWidth(columnInfos.get(1), 85.0);
    }

    @Test
    public void test2() {
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        columnSpecs.add(createColumnSpec("col_1", "22.2222*"));
        columnSpecs.add(createColumnSpec("col_2", "33.3333*"));
        columnSpecs.add(createColumnSpec("col_3", "44.4445*"));

        final var columnAdapter = TableHelper.buildColumns(TableType.PCT, 0, columnSpecs);
        final var columnInfos = columnAdapter.getColumns();
        printColumnInfos(columnInfos);
        verifyWidth(columnInfos.get(0), 22);
        verifyWidth(columnInfos.get(1), 33);
        verifyWidth(columnInfos.get(2), 44);
    }

    @Test
    public void test3() {
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        columnSpecs.add(createColumnSpec("col_1", "33.3333*"));
        columnSpecs.add(createColumnSpec("col_2", "33.3333*"));
        columnSpecs.add(createColumnSpec("col_3", "33.3333*"));

        final var columnAdapter = TableHelper.buildColumns(TableType.PCT, 0, columnSpecs);
        final var columnInfos = columnAdapter.getColumns();
        printColumnInfos(columnInfos);
        verifyWidth(columnInfos.get(0), 33);
        verifyWidth(columnInfos.get(1), 33);
        verifyWidth(columnInfos.get(2), 33);
    }

    @Test
    public void test4() {
        List<ColumnSpec> columnSpecs = new ArrayList<>();

        columnSpecs.add(createColumnSpec("col_1", "22*"));
        columnSpecs.add(createColumnSpec("col_2", "11*"));
        columnSpecs.add(createColumnSpec("col_3", "11*"));
        columnSpecs.add(createColumnSpec("col_4", "56*"));

        final var columnAdapter = TableHelper.buildColumns(TableType.PCT, 0, columnSpecs);
        final var columnInfos = columnAdapter.getColumns();
        printColumnInfos(columnInfos);
        verifyWidth(columnInfos.get(0), 22);
        verifyWidth(columnInfos.get(1), 11);
        verifyWidth(columnInfos.get(2), 11);
        verifyWidth(columnInfos.get(3), 56);
    }
}
