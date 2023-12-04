package com.alphasystem.docbook.util;

import com.alphasystem.openxml.builder.wml.TcPrBuilder;
import com.alphasystem.openxml.builder.wml.table.ColumnInfo;
import com.alphasystem.openxml.builder.wml.table.TableType;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner;

import java.math.BigDecimal;
import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblWidthBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTcPrBuilder;
import static java.lang.String.format;

/**
 * @author sali
 */
public final class TableAdapter {

    private static final String TYPE_PCT = TableType.PCT.getTableType();

    private TableAdapter() {
    }

    public static TcPr getColumnProperties(ColumnSpecAdapter columnSpecAdapter, Integer columnIndex, Integer gridSpanValue,
                                           VerticalMergeType verticalMergeType, TcPr columnProperties) throws ArrayIndexOutOfBoundsException {
        List<ColumnInfo> columnInfos = columnSpecAdapter.getColumnInfos();
        checkColumnIndex(columnInfos, columnIndex);
        final ColumnInfo columnInfo = columnInfos.get(columnIndex);
        BigDecimal columnWidth = new BigDecimal(columnInfo.getColumnWidth());
        long gs = 1;
        if (gridSpanValue != null && gridSpanValue > 1) {
            // sanity check, make sure we are not going out of bound
            checkColumnIndex(columnInfos, columnIndex + gridSpanValue - 1);
            // iterate through width and get the total width for the grid span
            for (int i = columnIndex + 1; i < columnIndex + gridSpanValue; i++) {
                final ColumnInfo columnInfo1 = columnInfos.get(i);
                columnWidth = columnWidth.add(new BigDecimal(columnInfo1.getColumnWidth()));
            }
            gs = gridSpanValue;
        }

        TcPrBuilder tcPrBuilder = getTcPrBuilder();
        TcPrInner.VMerge vMerge = null;
        if (verticalMergeType != null) {
            vMerge = tcPrBuilder.getVMergeBuilder().withVal(verticalMergeType.getValue()).getObject();
        }

        TblWidth tblWidth = getTblWidthBuilder().withType(TYPE_PCT).withW(columnWidth.longValue()).getObject();
        tcPrBuilder.withGridSpan(gs).withTcW(tblWidth).withVMerge(vMerge);

        return new TcPrBuilder(tcPrBuilder.getObject(), columnProperties).getObject();
    }

    // private methods

    /**
     * Checks whether <code>columnIndex</code> is within range.
     *
     * @param columnInfos column data
     * @param columnIndex index of column
     * @throws ArrayIndexOutOfBoundsException if <code>columnIndex</code> is out of bound.
     */
    private static void checkColumnIndex(List<ColumnInfo> columnInfos, int columnIndex)
            throws ArrayIndexOutOfBoundsException {
        int numOfColumns = columnInfos.size();
        if (columnIndex < 0 || columnIndex >= numOfColumns) {
            throw new ArrayIndexOutOfBoundsException(
                    format("Invalid columnIndex {%s}, expected values are between %s and %s",
                            columnIndex, 0, (numOfColumns - 1)));
        }
    }

    public enum VerticalMergeType {

        RESTART("restart"), CONTINUE(null);

        private final String value;

        VerticalMergeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
