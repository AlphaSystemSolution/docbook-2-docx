package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.util.ColumnSpecAdapter;
import com.alphasystem.docbook.util.TableAdapter;
import com.alphasystem.openxml.builder.wml.TblPrBuilder;
import org.docbook.model.*;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getDefaultBorder;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNilBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblBordersBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblPrBuilder;
import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public abstract class AbstractTableBuilder<T> extends BlockBuilder<T> {

    protected ColumnSpecAdapter columnSpecAdapter;
    protected Tbl table;

    protected AbstractTableBuilder(Builder parent, T source, int indexInParent) {
        super(parent, source, indexInParent);
    }

    protected String getTableStyle(String styleName) {
        final String tableStyle = configurationUtils.getTableStyle(styleName);
        if ((styleName != null) && (tableStyle == null)) {
            // styleName is not null but there is no corresponding style in DOCX
            logger.warn("No style defined for table with name \"{}\"", styleName);
        }
        return tableStyle;
    }

    protected void initializeTableAdapter(TableGroup tableGroup, Frame frame, String styleName) {
        int numOfColumns = Integer.parseInt(tableGroup.getCols());
        final List<ColumnSpec> colSpec = tableGroup.getColSpec();
        final boolean noColSpec = (colSpec == null) || colSpec.isEmpty();
        numOfColumns = noColSpec ? numOfColumns : colSpec.size();
        if (numOfColumns <= 0) {
            throw new RuntimeException("Neither numOfColumns nor colSpec defined.");
        }
        columnSpecAdapter = new ColumnSpecAdapter(colSpec);
        TblPrBuilder tblPrBuilder = getTblPrBuilder().withTblBorders(createFrame(frame));
        table = TableAdapter.getTable(columnSpecAdapter, getTableStyle(styleName), tblPrBuilder.getObject());
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        List<Object> result = new ArrayList<>();
        processedTitleContent.forEach(o -> result.add(o));
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

    private TblBorders createFrame(Frame frame) {
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
                insideH = getDefaultBorder();
                insideV = getDefaultBorder();
                break;
            case NONE:
                break;
        }
        return getTblBordersBuilder().withTop(top).withLeft(left).withBottom(bottom)
                .withRight(right).withInsideH(insideH).withInsideV(insideV).getObject();
    }

    @Override
    protected Builder getChildBuilder(Object o, int index) {
        final Builder childBuilder = super.getChildBuilder(o, index);
        if (isInstanceOf(TableContentBuilder.class, childBuilder)) {
            ((TableContentBuilder) childBuilder).setColumnSpecAdapter(columnSpecAdapter);
        }
        return childBuilder;
    }
}