package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import com.alphasystem.docbook.util.ColumnBuilder;
import com.alphasystem.openxml.builder.wml.table.ColumnInfo;
import com.alphasystem.openxml.builder.wml.table.ColumnInput;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import com.alphasystem.openxml.builder.wml.table.TableType;
import com.alphasystem.util.AppUtil;
import org.docbook.model.*;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.TblBorders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getDefaultBorder;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNilBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblBordersBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblPrBuilder;
import static java.lang.String.format;
import static org.docbook.model.Choice.ONE;

public abstract class AbstractTableBuilder<S> extends AbstractBuilder<S> {

    private static final int HEADER = 1;
    private static final int FOOTER = 2;


    private int level = -1;
    private final Map<Integer, ColumnBuilder.NextColumnInfo> nextColumnInfoMap = new HashMap<>();
    private List<ColumnInfo> columnInfoList;
    protected TableType tableType;
    protected TableAdapter tableAdapter;
    protected DocBookTableAdapter docBookTableAdapter;

    public TableAdapter getTableAdapter() {
        return tableAdapter;
    }

    @Override
    protected List<Object> getChildContent() {
        return Collections.emptyList();
    }

    @Override
    protected void doInit(S source) {
        super.doInit(source);
        this.level = Long.valueOf(ApplicationController.getContext().getCurrentListLevel()).intValue();
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var tableGroups = docBookTableAdapter.getTableGroup();
        final var tableGroup = ((tableGroups != null) && !tableGroups.isEmpty()) ? tableGroups.get(0) : null;
        if (tableGroup == null) {
            throw new IllegalArgumentException("tableGroup is null.");
        }
        initializeTableAdapter(tableGroup, docBookTableAdapter.getFrame(), docBookTableAdapter.getRowSep(),
                docBookTableAdapter.getColSep(), docBookTableAdapter.getTableStyle());
        buildHeader(tableGroup.getTableHeader());
        buildBody(tableGroup.getTableBody());
        buildFooter(tableGroup.getTableFooter());
        return Collections.singletonList(tableAdapter.getTable());
    }

    private void initializeTableAdapter(TableGroup tableGroup, Frame frame, Choice rowSep, Choice colSep, String styleName) {
        int numOfColumns = Integer.parseInt(tableGroup.getCols());
        final List<ColumnSpec> colSpec = tableGroup.getColSpec();
        final boolean noColSpec = (colSpec == null) || colSpec.isEmpty();
        numOfColumns = noColSpec ? numOfColumns : colSpec.size();
        if (numOfColumns <= 0) {
            throw new RuntimeException("Neither numOfColumns nor colSpec defined.");
        }

        tableType = level <= -1 ? TableType.PCT : TableType.AUTO;
        var tableStyle = getTableStyle(tableGroup, styleName);

        var tblPrBuilder = getTblPrBuilder().withTblBorders(createFrame(frame, rowSep, colSep));

        var tblPr = tblPrBuilder.getObject();
        tableAdapter = new TableAdapter().withTableType(tableType)
                .withTableStyle(tableStyle)
                .withIndentLevel(level)
                .withTableProperties(tblPr)
                .withColumnInputs(buildColumns(colSpec))
                .startTable();
        columnInfoList = tableAdapter.getColumns();
    }

    private String getTableStyle(TableGroup tableGroup, String styleName) {
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

    private void buildHeader(TableHeader tableHeader) {
        if (tableHeader != null) {
            final var vAlign = tableHeader.getVAlign();
            tableHeader.getRow().forEach(row -> buildRow(row, vAlign));
        }
    }

    private void buildBody(TableBody tableBody) {
        if (tableBody != null) {
            final var vAlign = tableBody.getVAlign();
            tableBody.getRow().forEach(row -> buildRow(row, vAlign));
        }
    }

    private void buildFooter(TableFooter tableFooter) {
        if (tableFooter != null) {
            final var vAlign = tableFooter.getVAlign();
            tableFooter.getRow().forEach(row -> buildRow(row, vAlign));
        }
    }

    private void buildRow(Row row, VerticalAlign verticalAlign) {
        tableAdapter.startRow();

        var columnIndex = 0;
        for (Object content : row.getContent()) {
            if (AppUtil.isInstanceOf(Entry.class, content)) {
                final var existingValue = nextColumnInfoMap.get(columnIndex);
                if (existingValue != null && existingValue.getMoreRows() > 0) {
                    columnIndex = existingValue.getNextColumnIndex();
                    final var updatedValue = existingValue.decrementMoreRows();
                    if (updatedValue.getMoreRows() > 0) {
                        nextColumnInfoMap.put(updatedValue.getCurrentColumnIndex(), updatedValue);
                    } else {
                        nextColumnInfoMap.remove(updatedValue.getCurrentColumnIndex());
                    }
                }
                var nextColumnInfo = ColumnBuilder.build(this, builderFactory, (Entry) content, columnIndex, verticalAlign);
                if (nextColumnInfo.getMoreRows() > 0) {
                    nextColumnInfoMap.put(nextColumnInfo.getCurrentColumnIndex(), nextColumnInfo);
                }
                columnIndex = nextColumnInfo.getNextColumnIndex();
            } else {
                throw new IllegalArgumentException(content.getClass().getName() + " is not implemented");
            }
        }

        nextColumnInfoMap.clear();
        tableAdapter.endRow();
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
            columnInputs[i] = new ColumnInput(columnSpec.getColumnName(), Double.parseDouble(columnWidth));
        }
        return columnInputs;
    }

    public int getGridSpan(String startColumnName, String endColumnName) {
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
        var columnInfos = columnInfoList.stream().filter(columnInfo -> columnInfo.getColumnName().equals(name))
                .collect(Collectors.toList());
        if (columnInfos.isEmpty()) {
            return null;
        } else {
            return columnInfos.get(0);
        }
    }
}
