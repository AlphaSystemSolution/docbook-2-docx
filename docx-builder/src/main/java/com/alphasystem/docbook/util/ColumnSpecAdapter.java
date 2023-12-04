package com.alphasystem.docbook.util;

import com.alphasystem.openxml.builder.wml.table.ColumnAdapter;
import com.alphasystem.openxml.builder.wml.table.ColumnInfo;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docbook.model.ColumnSpec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sali
 */
public final class ColumnSpecAdapter {

    private static final BigDecimal TOTAL_GRID_COL_WIDTH = TableAdapter.TOTAL_GRID_COL_WIDTH;
    private static final BigDecimal TOTAL_TABLE_WIDTH = TableAdapter.TOTAL_TABLE_WIDTH;
    private static final BigDecimal PERCENT = TableAdapter.PERCENT;
    private static final MathContext ROUNDING = TableAdapter.ROUNDING;

    private final ColumnAdapter columnAdapter;

    public ColumnSpecAdapter(List<ColumnSpec> columnSpecs) {
        this(PERCENT.doubleValue(), columnSpecs);
    }

    public ColumnSpecAdapter(double tableWidthInPercent, List<ColumnSpec> columnSpecs) {
        BigDecimal _w = BigDecimal.valueOf((tableWidthInPercent <= 0.0) ? PERCENT.doubleValue() : tableWidthInPercent);
        BigDecimal totalGridWidth = TOTAL_GRID_COL_WIDTH.multiply(_w).divide(PERCENT, ROUNDING);
        BigDecimal totalTableWidth = TOTAL_TABLE_WIDTH.multiply(_w).divide(PERCENT, ROUNDING);
        List<ColumnInfo> columnInfos = new ArrayList<>(columnSpecs.size());
        final int length = columnSpecs.size();
        BigDecimal[] widths = new BigDecimal[length];
        BigDecimal totalWidth = BigDecimal.ZERO;
        for (int i = 0; i < length; i++) {
            final ColumnSpec columnSpec = columnSpecs.get(i);
            String columnWidth = columnSpec.getColumnWidth();
            if (columnWidth.endsWith("*")) {
                columnWidth = columnWidth.substring(0, columnWidth.length() - 1);
            }
            final BigDecimal width = new BigDecimal(Double.parseDouble(columnWidth), ROUNDING);
            widths[i] = width;
            totalWidth = totalWidth.add(width, ROUNDING);
        }

        for (int i = 0; i < length; i++) {
            final ColumnSpec columnSpec = columnSpecs.get(i);
            BigDecimal columnWidthInPercent = widths[i].multiply(PERCENT).divide(totalWidth, ROUNDING);
            final double columnWidth = totalTableWidth.multiply(columnWidthInPercent).divide(PERCENT, ROUNDING).doubleValue();
            final double gridWidth = totalGridWidth.multiply(columnWidthInPercent).divide(PERCENT, ROUNDING).doubleValue();
            columnInfos.add(new ColumnInfo(i, columnSpec.getColumnName(), columnWidth, gridWidth));
        }

        columnAdapter = new ColumnAdapter(totalTableWidth, columnInfos);
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnAdapter.getColumns();
    }

    public BigDecimal getTotalTableWidth() {
        return columnAdapter.getTotalTableWidth();
    }

    public ColumnAdapter getColumnAdapter() {
        return columnAdapter;
    }

    public ColumnInfo getColumnInfo(String name) {
        var columnInfos = getColumnInfos().stream().filter(columnInfo -> columnInfo.getColumnName().equals(name))
                .collect(Collectors.toList());
        if (columnInfos.isEmpty()) {
            return null;
        } else {
            return columnInfos.get(0);
        }
    }
}
