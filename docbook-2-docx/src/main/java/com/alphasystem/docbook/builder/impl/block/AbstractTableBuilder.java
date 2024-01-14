package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import com.alphasystem.openxml.builder.wml.table.ColumnInfo;
import com.alphasystem.openxml.builder.wml.table.ColumnInput;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import com.alphasystem.openxml.builder.wml.table.TableType;
import org.docbook.model.Choice;
import org.docbook.model.ColumnSpec;
import org.docbook.model.Frame;
import org.docbook.model.TableGroup;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getDefaultBorder;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNilBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblBordersBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblPrBuilder;
import static java.lang.String.format;
import static org.docbook.model.Choice.ONE;

public abstract class AbstractTableBuilder<S> extends BlockBuilder<S> {

    private static final int HEADER = 1;
    private static final int FOOTER = 2;

    private int level = -1;
    private List<ColumnInfo> columnInfoList;
    protected TableType tableType;
    private Tbl table;
    private TableGroup tableGroup;
    protected DocBookTableAdapter docBookTableAdapter;

    protected AbstractTableBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }

    public TableType getTableType() {
        return tableType;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    @Override
    protected List<Object> getChildContent() {
        final var childContent = new ArrayList<>();

        final var header = tableGroup.getTableHeader();
        if (!Objects.isNull(header)) {
            childContent.add(header);
        }

        final var body = tableGroup.getTableBody();
        if (!Objects.isNull(body)) {
            childContent.add(body);
        }

        final var footer = tableGroup.getTableFooter();
        if (!Objects.isNull(footer)) {
            childContent.add(footer);
        }
        return childContent;
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        this.level = -1;
        final var listParent = getParent(ListBuilder.class);
        if (listParent != null) {
            this.level = (int) listParent.getListInfo().getLevel();
        }
        final var tableGroups = docBookTableAdapter.getTableGroup();
        tableGroup = ((tableGroups != null) && !tableGroups.isEmpty()) ? tableGroups.get(0) : null;
        if (tableGroup == null) {
            throw new IllegalArgumentException("tableGroup is null.");
        }
        initializeTable(tableGroup, docBookTableAdapter.getFrame(), docBookTableAdapter.getRowSep(),
                docBookTableAdapter.getColSep(), docBookTableAdapter.getTableStyle());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        table.getContent().addAll(processedChildContent);
        return Collections.singletonList(table);
    }

    private void initializeTable(TableGroup tableGroup, Frame frame, Choice rowSep, Choice colSep, String styleName) {
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

        final var tableAdapter = new TableAdapter()
                .withTableType(tableType)
                .withTableStyle(tableStyle)
                .withIndentLevel(level)
                .withTableProperties(tblPrBuilder.getObject())
                .withColumnInputs(buildColumns(colSpec))
                .startTable();
        columnInfoList = tableAdapter.getColumns();
        table = tableAdapter.getTable();
    }

    private String getTableStyle(TableGroup tableGroup, String styleName) {
        var tableStyle = configurationUtils.getTableStyle(styleName);
        if ((styleName != null) && (tableStyle == null)) {
            tableStyle = styleName;
        }
        if (Objects.isNull(tableStyle)) {
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
