package com.alphasystem.docbook.util;

import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.builder2.impl.block.AbstractTableBuilder;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.VerticalMergeType;
import com.alphasystem.util.AppUtil;
import com.alphasystem.xml.UnmarshallerConstants;
import com.alphasystem.xml.UnmarshallerUtils;
import jakarta.xml.bind.JAXBElement;
import org.docbook.model.Align;
import org.docbook.model.BasicVerticalAlign;
import org.docbook.model.Entry;
import org.docbook.model.VerticalAlign;
import org.docx4j.wml.*;

import java.util.*;
import java.util.stream.Collectors;

public class ColumnBuilder {

    private ColumnBuilder() {
    }

    /**
     * @param parentBuilder       table builder
     * @param entry               DocBook column entry
     * @param columnIndex         current columnIndex
     * @param parentVerticalAlign Table VerticalAlign
     * @return next column info
     */
    public static NextColumnInfo build(AbstractTableBuilder<?> parentBuilder, BuilderFactory builderFactory,
                                       Entry entry, int columnIndex, VerticalAlign parentVerticalAlign) {
        final var tcPr = WmlBuilderFactory.getTcPrBuilder().withVAlign(getVerticalAlign(entry.getValign(), parentVerticalAlign))
                .getObject();
        final var gridSpan = parentBuilder.getGridSpan(entry.getNameStart(), entry.getNameEnd());
        final var moreRows = UnmarshallerUtils.toInt(entry.getMoreRows(), 0);
        VerticalMergeType vMergeType = moreRows <= 0 ? VerticalMergeType.CONTINUE : VerticalMergeType.RESTART;

        final var columnContent = entry.getContent().stream().map(content -> {
            var align = getAlign(entry.getAlign());
            final var processedContent = builderFactory.process(content, parentBuilder);
            if (UnmarshallerConstants.isParaTypes(content)) {
                var p = (P) (processedContent.get(0));
                final var ppr = new PPrBuilder(WmlBuilderFactory.getPPrBuilder().withJc(align).getObject(), p.getPPr()).getObject();
                p.setPPr(ppr);
                return Collections.singletonList(p);
            } else {
                return processedContent;
            }
        }).flatMap(Collection::stream).toArray();


        var columnData = new ColumnData(columnIndex).withColumnProperties(tcPr).withGridSpanValue(gridSpan)
                .withVerticalMergeType(vMergeType).withContent(columnContent);

        return new NextColumnInfo(moreRows, columnIndex, columnIndex + gridSpan, columnData);
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

    private static JcEnumeration getAlign(Align align) {
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

    public static class NextColumnInfo {

        private final int moreRows;
        private final int currentColumnIndex;
        private final int nextColumnIndex;
        private final ColumnData columnData;

        public NextColumnInfo(int moreRows, int currentColumnIndex, int nextColumnIndex, ColumnData columnData) {
            this.moreRows = Math.max(moreRows, 0);
            this.currentColumnIndex = Math.max(currentColumnIndex, 0);
            this.nextColumnIndex = Math.max(nextColumnIndex, 0);
            this.columnData = columnData;
        }

        public int getMoreRows() {
            return moreRows;
        }

        public int getCurrentColumnIndex() {
            return currentColumnIndex;
        }

        public int getNextColumnIndex() {
            return nextColumnIndex;
        }

        public ColumnData getColumnData() {
            return columnData;
        }

        public NextColumnInfo decrementMoreRows() {
            return new NextColumnInfo(moreRows - 1, currentColumnIndex, nextColumnIndex, columnData);
        }

        @Override
        public String toString() {
            return "NextColumnInfo (" +
                    "moreRows=" + moreRows +
                    ", currentColumnIndex=" + currentColumnIndex +
                    ", nextColumnIndex=" + nextColumnIndex +
                    ")";
        }
    }

    // TODO: remove it

    private static String getRawText(Object content) {
        if (AppUtil.isInstanceOf(P.class, content)) {
            final var p = (P) content;
            return getRawText("", p.getContent());
        } else {
            return content.getClass().getName() + " ";
        }
    }

    private static String getRawText(String result, List<Object> contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            return result;
        }

        return contents.stream().map(content -> {
            if (AppUtil.isInstanceOf(R.class, content)) {
                final var r = (R) content;
                return getRawText(result, r.getContent());
            } else if (AppUtil.isInstanceOf(Text.class, content)) {
                final var text = (Text) content;
                return result + text.getValue();
            } else if (AppUtil.isInstanceOf(P.Hyperlink.class, content)) {
                final var hyperlink = (P.Hyperlink) content;
                return getRawText(result, hyperlink.getContent());
            } else if (AppUtil.isInstanceOf(CTBookmark.class, content) ||
                    AppUtil.isInstanceOf(JAXBElement.class, content)) {
                return "";
            } else {
                return content.getClass().getName() + " ";
            }
        }).collect(Collectors.joining());
    }
    // TODO
}
