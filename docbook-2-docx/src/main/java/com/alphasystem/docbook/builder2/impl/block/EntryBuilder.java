package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import com.alphasystem.util.AppUtil;
import com.alphasystem.xml.UnmarshallerConstants;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.Align;
import org.docbook.model.BasicVerticalAlign;
import org.docbook.model.Entry;
import org.docbook.model.VerticalAlign;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.TcPr;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntryBuilder extends AbstractBuilder<Entry> {

    private TcPr tcPr;
    private JcEnumeration align;

    public EntryBuilder(Entry source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected void doInit(Entry source, Builder<?> parent) {
        super.doInit(source, parent);

        final var contentBuilder = getParent().getParent();
        if (!AppUtil.isInstanceOf(TableContentBuilder.class, contentBuilder)) {
            // EntryBuilder immediate parent should be RowBuilder and RowBuilder parent must be either of TableBodyBuilder,
            // TableHeaderBuilder, or TableFooterBuilder, if not raise exception
            throw new RuntimeException(String.format("Found different parent \"%s\".", contentBuilder.getClass().getName()));
        }

        final var parentVAlign = (VerticalAlign) Utils.invokeMethod(contentBuilder.getSource(), "getVAlign");
        final var verticalAlign = getVerticalAlign(source.getValign(), parentVAlign);

        final var parentAlign = (Align) Utils.invokeMethod(contentBuilder.getSource(), "getAlign");
        align = getAlign(source.getAlign(), parentAlign);

        tcPr = WmlBuilderFactory.getTcPrBuilder().withVAlign(verticalAlign).getObject();
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        if (Objects.isNull(childContent) || childContent.isEmpty()) {
            return Collections.singletonList(WmlAdapter.getEmptyPara());
        } else {
            return childContent.stream().map(content -> {
                final var processedContent = builderFactory.process(content, this);
                if (UnmarshallerConstants.isParaTypes(content)) {
                    var p = (P) (processedContent.get(0));
                    final var ppr = new PPrBuilder(WmlBuilderFactory.getPPrBuilder().withJc(align).getObject(), p.getPPr()).getObject();
                    p.setPPr(ppr);
                    return Collections.singletonList(p);
                } else {
                    return processedContent;
                }
            }).flatMap(Collection::stream).collect(Collectors.toList());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Object> doProcess(List<Object> processedChildContent) {
        final var tableBuilder = getParent(AbstractTableBuilder.class);
        final var gridSpan = tableBuilder.getGridSpan(source.getNameStart(), source.getNameEnd());
        final var moreRows = source.getMoreRows();
        final var vMergeType = StringUtils.isBlank(moreRows) ? VerticalMergeType.NONE : VerticalMergeType.valueOf(moreRows);
        final var parent = (RowBuilder) getParent();
        final var columnIndex = parent.getColumnIndex();
        parent.updateColumnIndex(columnIndex + gridSpan);
        var columnData = new ColumnData(columnIndex).withColumnProperties(tcPr).withGridSpanValue(gridSpan)
                .withVerticalMergeType(vMergeType).withContent(processedChildContent.toArray());
        final var tc = TableAdapter.createColumn(tableBuilder.getTableType(), columnData, tableBuilder.getColumnInfoList());
        return Collections.singletonList(tc);
    }

    private static STVerticalJc getVerticalAlign(BasicVerticalAlign vAlign, VerticalAlign parentVerticalAlign) {
        if (vAlign == null && parentVerticalAlign != null) {
            try {
                vAlign = BasicVerticalAlign.fromValue(parentVerticalAlign.value());
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

    private static JcEnumeration getAlign(Align align, Align parentAlign) {
        if (align == null && parentAlign != null) {
            align = parentAlign;
        }
        JcEnumeration jcEnumeration = null;
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
