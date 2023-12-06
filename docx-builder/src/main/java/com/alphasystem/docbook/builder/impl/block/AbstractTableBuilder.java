package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.TblPrBuilder;
import com.alphasystem.openxml.builder.wml.table.*;
import org.docbook.model.*;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getDefaultBorder;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNilBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblBordersBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblPrBuilder;
import static java.lang.String.format;
import static org.docbook.model.Choice.ONE;

/**
 * @author sali
 */
public abstract class AbstractTableBuilder<T> extends BlockBuilder<T> {

    private static final int HEADER = 1;
    private static final int FOOTER = 2;


    private ColumnAdapter columnAdapter;
    private TableType tableType;
    protected Tbl table;

    protected AbstractTableBuilder(Builder<?> parent, T source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    protected String getTableStyle(TableGroup tableGroup, String styleName) {
        String tableStyle = configurationUtils.getTableStyle(styleName);
        if ((styleName != null) && (tableStyle == null)) {
            // styleName is not null but there is no corresponding style in Word document
            logger.warn("No style defined for table with name \"{}\"", styleName);
        }
        if (tableStyle == null) {
            int header = (tableGroup.getTableHeader() == null) ? 0 : HEADER;
            int footer = (tableGroup.getTableFooter() == null) ? 0 : FOOTER;
            int value = header | footer;
            if (value != 0) {
                tableStyle = format("TableGrid%s", value);
            }
        }
        return tableStyle;
    }

    protected void initializeTableAdapter(TableGroup tableGroup, Frame frame, Choice rowSep, Choice colSep, String styleName) {
        int numOfColumns = Integer.parseInt(tableGroup.getCols());
        final List<ColumnSpec> colSpec = tableGroup.getColSpec();
        final boolean noColSpec = (colSpec == null) || colSpec.isEmpty();
        numOfColumns = noColSpec ? numOfColumns : colSpec.size();
        if (numOfColumns <= 0) {
            throw new RuntimeException("Neither numOfColumns nor colSpec defined.");
        }

        final ListItemBuilder listItemBuilder = getParent(ListItemBuilder.class);
        int level = -1;
        if (listItemBuilder != null) {
            level = (int) listItemBuilder.getLevel();
        }

        tableType = level <= -1 ? TableType.PCT : TableType.AUTO;
        var tableStyle = getTableStyle(tableGroup, styleName);

        TblPrBuilder tblPrBuilder = getTblPrBuilder().withTblBorders(createFrame(frame, rowSep, colSep));

        var tblPr = tblPrBuilder.getObject();
        var tableAdapter = new TableAdapter().withTableType(tableType)
                .withTableStyle(tableStyle)
                .withIndentLevel(level)
                .withTableProperties(tblPr)
                .withColumnInputs(buildColumns(colSpec))
                .startTable();
        columnAdapter = tableAdapter.getColumnAdapter();
        table = tableAdapter
                .getTable();
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        List<Object> result = new ArrayList<>(processedTitleContent);
        processedChildContent.forEach(o -> table.getContent().add(o));
        result.add(table);
        return result;
    }

    protected void initializeContent(TableGroup tableGroup) {
        content = new ArrayList<>();
        final TableHeader tableHeader = tableGroup.getTableHeader();
        if (tableHeader != null) {
            content.add(tableHeader);
        }
        final TableBody tableBody = tableGroup.getTableBody();
        if (tableBody != null) {
            content.add(tableBody);
        }
        final TableFooter tableFooter = tableGroup.getTableFooter();
        if (tableFooter != null) {
            content.add(tableFooter);
        }
    }

    private TblBorders createFrame(Frame frame, Choice rowSep, Choice colSep) {
        frame = (frame == null) ? Frame.NONE : frame;
        CTBorder top = getNilBorder();
        CTBorder left = getNilBorder();
        CTBorder bottom = getNilBorder();
        CTBorder right = getNilBorder();
        CTBorder insideH = getNilBorder();
        CTBorder insideV = getNilBorder();

        switch (frame) {
            case ABOVE:
            case TOP:
                top = getDefaultBorder();
                break;
            case BELOW:
            case BOTTOM:
                bottom = getDefaultBorder();
                break;
            case TOP_AND_BOTTOM:
                top = getDefaultBorder();
                bottom = getDefaultBorder();
                break;
            case LEFT_HAND_SIDE:
                left = getDefaultBorder();
                break;
            case RIGHT_HAND_SIDE:
                right = getDefaultBorder();
                break;
            case SIDES:
                left = getDefaultBorder();
                right = getDefaultBorder();
                break;
            case HORIZONTAL_SIDES:
                insideH = getDefaultBorder();
                break;
            case VERTICAL_SIDES:
                insideV = getDefaultBorder();
                break;
            case BOX:
            case BORDER:
                top = getDefaultBorder();
                left = getDefaultBorder();
                bottom = getDefaultBorder();
                right = getDefaultBorder();
                break;
            case ALL:
                top = getDefaultBorder();
                left = getDefaultBorder();
                bottom = getDefaultBorder();
                right = getDefaultBorder();
                break;
            case NONE:
                break;
        }
        if (ONE.equals(rowSep)) {
            insideH = getDefaultBorder();
        }
        if (ONE.equals(colSep)) {
            insideV = getDefaultBorder();
        }
        return getTblBordersBuilder().withTop(top).withLeft(left).withBottom(bottom)
                .withRight(right).withInsideH(insideH).withInsideV(insideV).getObject();
    }

    public TableType getTableType() {
        return tableType;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnAdapter.getColumns();
    }

    int getGridSpan(String startColumnName, String endColumnName) {
        int gridSpan = 1;
        if (startColumnName != null && endColumnName != null) {
            final ColumnInfo startColumn = getColumnInfo(startColumnName);
            if (startColumn == null) {
                throw new RuntimeException(format("No column info found with name \"%s\".", startColumnName));
            }
            final ColumnInfo endColumn = getColumnInfo(endColumnName);
            if (endColumn == null) {
                throw new RuntimeException(format("No column info found with name \"%s\".", endColumnName));
            }
            final int startColumnColumnNumber = startColumn.getColumnNumber();
            final int endColumnColumnNumber = endColumn.getColumnNumber();
            gridSpan = endColumnColumnNumber - startColumnColumnNumber + 1;
            if (gridSpan < 1) {
                throw new RuntimeException(format("Invalid start (%s) and end (%s) column indices for columns \"%s\" and \"%s\" respectively.",
                        startColumnColumnNumber, endColumnColumnNumber, startColumnName, endColumnName));
            }
        }
        return gridSpan;
    }

    private ColumnInfo getColumnInfo(String name) {
        var columnInfos = getColumnInfos().stream().filter(columnInfo -> columnInfo.getColumnName().equals(name))
                .collect(Collectors.toList());
        if (columnInfos.isEmpty()) {
            return null;
        } else {
            return columnInfos.get(0);
        }
    }

    private static ColumnInput[] buildColumns(List<ColumnSpec> columnSpecs) {
        if (columnSpecs == null || columnSpecs.isEmpty()) {
            throw new IllegalArgumentException("Invalid column spec");
        }
        final var numOfColumns = columnSpecs.size();

        var columnInputs = new ColumnInput[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            final var columnSpec = columnSpecs.get(i);
            var columnWidth = columnSpec.getColumnWidth();
            if (columnWidth.endsWith("*")) {
                columnWidth = columnWidth.substring(0, columnWidth.length() - 1);
            }
            columnInputs[i] = new ColumnInput(columnSpec.getColumnName(),  Double.parseDouble(columnWidth));
        }
        return columnInputs;
    }
}
