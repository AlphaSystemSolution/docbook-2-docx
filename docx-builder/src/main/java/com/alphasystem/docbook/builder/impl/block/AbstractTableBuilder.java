package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.util.TableAdapter;
import com.alphasystem.openxml.builder.wml.TblPrBuilder;
import org.docbook.model.ColumnSpec;
import org.docbook.model.Frame;
import org.docbook.model.TableGroup;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;

import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getDefaultBorder;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNilBorder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblBordersBuilder;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getTblPrBuilder;

/**
 * @author sali
 */
public abstract class AbstractTableBuilder<T> extends BlockBuilder<T> {

    protected TableAdapter tableAdapter;
    protected Tbl table;

    protected AbstractTableBuilder(Builder parent, T source) {
        super(parent, source);
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
        tableAdapter = new TableAdapter(colSpec);
        TblPrBuilder tblPrBuilder = getTblPrBuilder().withTblBorders(createFrame(frame));
        table = tableAdapter.getTable(getTableStyle(styleName), tblPrBuilder.getObject());
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

}
