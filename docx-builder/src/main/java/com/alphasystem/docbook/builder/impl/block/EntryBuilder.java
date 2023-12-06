package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import com.alphasystem.openxml.builder.wml.TcBuilder;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import org.docbook.model.Align;
import org.docbook.model.BasicVerticalAlign;
import org.docbook.model.Entry;
import org.docbook.model.VerticalAlign;
import org.docx4j.wml.*;

import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getEmptyPara;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.*;
import static com.alphasystem.util.AppUtil.isInstanceOf;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * @author sali
 */
public class EntryBuilder extends BlockBuilder<Entry> {

    protected Tc column;

    public EntryBuilder(Builder<?> parent, Entry entry, int indexInParent) {
        super(parent, entry, indexInParent);
    }

    @Override
    protected void initContent() {
        initializeColumn();
        content = source.getContent();
    }

    private void initializeColumn() {
        final AbstractTableBuilder<?> tableBuilder = getParent(AbstractTableBuilder.class);
        TcPr tcPr = getTcPrBuilder().withVAlign(getVerticalAlign()).getObject();

        int columnIndex = indexInParent;
        int gridSpan = tableBuilder.getGridSpan(source.getNameStart(), source.getNameEnd());
        ((RowBuilder) getParent()).updateNextColumnIndex(gridSpan);

        final String moreRows = source.getMoreRows();
        VerticalMergeType vMergeType = null;
        if (moreRows != null) {
            vMergeType = moreRows.endsWith("*") ? VerticalMergeType.CONTINUE : VerticalMergeType.RESTART;
        }
        tcPr = TableAdapter.getColumnProperties(tableBuilder.getTableType(), columnIndex, gridSpan, vMergeType, tcPr,
                tableBuilder.getColumnInfos());
        TcBuilder tcBuilder = getTcBuilder().withTcPr(tcPr);
        column = tcBuilder.getObject();
    }

    @Override
    protected List<Object> postProcess(List<Object> processedTitleContent, List<Object> processedChildContent) {
        if (processedChildContent.isEmpty()) {
            column.getContent().add(getEmptyPara());
        } else {
            JcEnumeration align = getAlign();
            processedChildContent.forEach(o -> {
                if (isInstanceOf(P.class, o)) {
                    PPrBuilder pPrBuilder = getPPrBuilder().withJc(align);
                    final P p = (P) o;
                    pPrBuilder = new PPrBuilder(pPrBuilder.getObject(), p.getPPr());
                    p.setPPr(pPrBuilder.getObject());
                }
                column.getContent().add(o);
            });
        }
        return singletonList(column);
    }

    private STVerticalJc getVerticalAlign() {
        final Builder<?> parent = getParent().getParent();
        if (!isInstanceOf(TableContentBuilder.class, parent)) {
            // EntryBuilder immediate parent should be RowBuilder and RowBuilder parent must be either of TableBodyBuilder,
            // TableHeaderBuilder, or TableFooterBuilder, if not raise exception
            throw new RuntimeException(format("Found different parent \"%s\".", parent.getClass().getName()));
        }
        final Object parentSource = parent.getSource();
        final VerticalAlign parentAlign = (VerticalAlign) invokeMethod(parentSource, "getVAlign");
        BasicVerticalAlign vAlign = source.getValign();

        if (vAlign == null && parentAlign != null) {
            try {
                vAlign = BasicVerticalAlign.fromValue(parentAlign.value());
            } catch (Exception e) {
                // ignore, if the parent value is BASELINE, then there is no corresponding value for BasicVerticalAlign
            }
        }
        STVerticalJc val = null;
        if (vAlign != null) {
            switch (vAlign) {
                case BOTTOM:
                    val = STVerticalJc.BOTTOM;
                    break;
                case MIDDLE:
                    val = STVerticalJc.CENTER;
                    break;
                case TOP:
                    val = STVerticalJc.TOP;
                    break;
            }
        }
        return val;
    }

    private JcEnumeration getAlign() {
        JcEnumeration jcEnumeration = null;
        Align align = source.getAlign();
        if (align != null) {
            switch (align) {
                case LEFT:
                    jcEnumeration = JcEnumeration.LEFT;
                    break;
                case CENTER:
                    jcEnumeration = JcEnumeration.CENTER;
                    break;
                case RIGHT:
                    jcEnumeration = JcEnumeration.RIGHT;
                    break;
                case JUSTIFY:
                    jcEnumeration = JcEnumeration.BOTH;
                    break;
            }
        }
        return jcEnumeration;
    }

}
